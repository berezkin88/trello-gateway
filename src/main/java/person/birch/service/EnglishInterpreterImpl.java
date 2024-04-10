package person.birch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.model.Report;
import person.birch.model.ReportsContainer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Singleton
public class EnglishInterpreterImpl implements EnglishInterpreter {

    private static final Logger LOG = LoggerFactory.getLogger(EnglishInterpreterImpl.class);
    private final ReportMapper reportMapper = Mappers.getMapper(ReportMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ReportsContainer translateToEng(Uni<String> input) throws TimeoutException, ExecutionException, InterruptedException {
        var ukrReports = getUkrReports(input);
        var engReport = ukrReports.stream()
            .map(reportMapper::clone)
            .toList();
        return new ReportsContainer(ukrReports, engReport);
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

    @Mapper
    interface ReportMapper {
        @Mapping(target = "currency", constant = "shared.currency.eur")
        Report clone(Report report);
    }
}
