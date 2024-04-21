package person.birch.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ReportRetrieverImplTest {

    private ReportRetriever reportRetriever;
    @Inject
    TrelloGateway trelloGateway;
    @Inject
    ReportsBuilder reportsBuilder;
    @Inject
    ImageConveyor imageConveyor;

    @BeforeEach
    void setUp() {
        reportRetriever = new ReportRetrieverImpl(trelloGateway, reportsBuilder, imageConveyor);
    }

    @Test
    void getReports() {
        var reports = reportRetriever.getReports("квітень", "2024");
        Assertions.assertNotNull(reports);
    }
}