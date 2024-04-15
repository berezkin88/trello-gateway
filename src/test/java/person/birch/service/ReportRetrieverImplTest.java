package person.birch.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.birch.category.CategoriesConfig;

@QuarkusTest
class ReportRetrieverImplTest {

    private ReportRetriever reportRetriever;
    @Inject
    TrelloGateway trelloGateway;
    @Inject
    CategoriesConfig categoriesConfig;

    @BeforeEach
    void setUp() {
        reportRetriever = new ReportRetrieverImpl(trelloGateway);
    }

    @Test
    void getReports() {
        var reports = reportRetriever.getReports();

        Assertions.assertNotNull(reports);
    }
}