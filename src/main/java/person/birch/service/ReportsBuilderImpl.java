package person.birch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.model.Report;
import person.birch.model.ReportsContext;
import person.birch.model.TrelloItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Singleton
public class ReportsBuilderImpl implements ReportsBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ReportsBuilderImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GTranslatorService gTranslatorService;

    public ReportsBuilderImpl(GTranslatorService gTranslatorService) {
        this.gTranslatorService = gTranslatorService;
    }

    @Override
    public ReportsContext buildReports(Uni<String> input) throws TimeoutException, ExecutionException, InterruptedException {
        var originalReports = getUkrReports(input);
        var descriptionUrk = getDescriptionUrk(originalReports);
        var descriptionEng = translateDescription(descriptionUrk);
        updateReportsDescriptions(originalReports, descriptionUrk);
        return new ReportsContext(descriptionUrk, descriptionEng, new TrelloItem(originalReports));
    }

    private List<Report> getUkrReports(Uni<String> input) throws InterruptedException, ExecutionException, TimeoutException {
        return input.map(str -> {
                try {
                    return objectMapper.readValue(str, objectMapper.getTypeFactory().constructCollectionType(List.class, Report.class));
                } catch (JsonProcessingException e) {
                    LOG.error("Failed to parse reports", e);
                    return List.<Report>of();
                }
            })
            .subscribeAsCompletionStage()
            .get(10, TimeUnit.SECONDS);
    }

    private Map<String, String> getDescriptionUrk(List<Report> reports) {
        if (null == reports || reports.isEmpty()) {
            return Map.of();
        }

        LOG.info("Збираються описи...");
        var atomicInteger = new AtomicInteger();
        return reports.stream()
            .collect(
                Collectors.toMap(
                    report -> "%s-%d".formatted(parseDate(report.getDate()), atomicInteger.incrementAndGet()),
                    Report::getDescription
                )
            );
    }

    /**
     * @param date input in format like `12.07.2023`
     * @return a date formatted like `07-2023` (month-year)
     */
    private String parseDate(String date) {
        if (null == date || date.isEmpty()) {
            var now = LocalDate.now();
            return "%d-%d".formatted(now.getMonthValue(), now.getYear());
        }

        var slit = date.split("\\.");

        if (3 != slit.length) {
            var now = LocalDate.now();
            return "%d-%d".formatted(now.getMonthValue(), now.getYear());
        }

        return "%s-%s".formatted(slit[1], slit[2]);
    }

    private Map<String, String> translateDescription(Map<String, String> descriptionUkr) {
        if (null == descriptionUkr || descriptionUkr.isEmpty() || null == gTranslatorService) {
            return descriptionUkr;
        }

        LOG.info("Йде переклад опису...");
        var mapEng = new HashMap<>(descriptionUkr);

        mapEng.forEach((key, value) -> {
            try {
                mapEng.put(key, gTranslatorService.translate(value));
            } catch (IOException | URISyntaxException e) {
                LOG.error("Failed to translate value", e);
            }
        });

        LOG.info("Переклад завершено");
        return mapEng;
    }

    // weak spot, think how to improve
    private void updateReportsDescriptions(List<Report> originalReports, Map<String, String> descriptionUrk) {
        if (null == originalReports || null == descriptionUrk) {
            return;
        }

        descriptionUrk.forEach((key, value) -> {
            for (var report : originalReports) {
                if (value.equals(report.getDescription())) {
                    report.setDescription("reports.descriptions.%s".formatted(key));
                }
            }
        });
    }
}
