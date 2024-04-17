package person.birch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.model.ReportsContext;
import person.birch.model.TrelloList;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Singleton
public class ReportRetrieverImpl implements ReportRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(ReportRetrieverImpl.class);
    private static final Locale UK_LOCALE = Locale.of("uk", "UA");
    private static final Stream<String> UA_MONTHS = Arrays.stream(Month.values())
        .map(m -> m.getDisplayName(TextStyle.FULL_STANDALONE, UK_LOCALE));

    private final TrelloGateway trelloGateway;
    private final ReportsBuilder reportsBuilder;

    public ReportRetrieverImpl(TrelloGateway trelloGateway,
                               ReportsBuilder reportsBuilder) {
        this.trelloGateway = trelloGateway;
        this.reportsBuilder = reportsBuilder;
    }

    /**
     * Get reports for a targeted month and year. Params are optional,
     * if none provided reports for a current month and year is retrieved
     * if such exists.
     * </p>
     * @param args optional targeted month and year
     * @return report of two different languages
     */

    @Override
    public ReportsContext getReports(String... args) {
        var monthsAndYear = resolveArgs(args);

        ReportsContext reportsContext = null;
        try {
            var lists = trelloGateway.getLists();
            LOG.info("Отримання всіх доступних звітів...");
            var listId = getListId(lists, monthsAndYear);
            LOG.info("Отримання обраних звітів...");
            var cards = trelloGateway.getListDetails(listId);
            LOG.info("Перепакування звітів...");
            reportsContext = reportsBuilder.buildReports(cards);
        } catch (URISyntaxException | ExecutionException | TimeoutException | JsonProcessingException e) {
            LOG.error("Failed to retrieve Lists from the Board", e);
            return null;
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Звіти створено");
        return reportsContext;
    }

    /**
     * Attempt to resolve user input as month or year in Ukrainian language.
     * <p>
     * Expecting 1st arg to be a month in UA, 2nd a year.
     * If only one arg exists, implying it's a month. Will return with a current year.
     * If none, return current month and year.
     * <p>
     * Safe to input anything and any number of args.
     *
     * @param args supposedly month and year.
     * @return month and year as a String in Ukrainian.
     */
    private String resolveArgs(String... args) {
        var date = LocalDate.now();
        // no args
        if (args == null || args.length == 0) {
            var month = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, UK_LOCALE);
            var year = date.getYear();
            return "%s %d".formatted(month, year);
        }
        // one arg
        if (args.length == 1) {
            var year = date.getYear();
            var month = UA_MONTHS.filter(m -> args[0].toLowerCase(UK_LOCALE).equals(m))
                .findFirst()
                .orElse(date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, UK_LOCALE));
            return "%s %d".formatted(month, year);
        }
        // two or more
        var monthCandidate = args[0].toLowerCase(UK_LOCALE);
        var year = parseYear(args[1]);
        var month = UA_MONTHS.filter(monthCandidate::equals)
            .findFirst()
            .orElse(date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, UK_LOCALE));

        return "%s %d".formatted(month, year);
    }

    private int parseYear(String year) {
        var patter = Pattern.compile("\\d{4}");
        var matcher = patter.matcher(year);

        if (matcher.find()) {
            var yearNum = Integer.parseInt(matcher.group());
            if (2030 > yearNum && 2022 <= yearNum) {
                return yearNum;
            }
        }

        return LocalDate.now().getYear();
    }

    private String getListId(Uni<String> lists, String monthsAndYear) throws ExecutionException, InterruptedException, TimeoutException {
        var objectMapper = new ObjectMapper();
        return lists.map(str -> {
                try {
                    return objectMapper.readValue(str, objectMapper.getTypeFactory().constructCollectionType(List.class, TrelloList.class));
                } catch (JsonProcessingException e) {
                    LOG.error("Failed to parse reports", e);
                    return List.<TrelloList>of();
                }
            })
            .subscribeAsCompletionStage()
            .get(10, TimeUnit.SECONDS)
            .stream()
            .filter(tl -> monthsAndYear.equalsIgnoreCase(tl.name()))
            .map(TrelloList::id)
            .findFirst()
            .orElse("bogus");
    }
}
