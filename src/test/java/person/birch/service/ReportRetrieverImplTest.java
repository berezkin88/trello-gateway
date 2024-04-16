package person.birch.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.birch.category.CategoryService;

import java.io.IOException;

@QuarkusTest
class ReportRetrieverImplTest {

    private ReportRetriever reportRetriever;
    @Inject
    TrelloGateway trelloGateway;
    @Inject
    EnglishInterpreter englishInterpreter;
    @Inject
    CategoryService categoryService;


    @BeforeEach
    void setUp() throws IOException {
        reportRetriever = new ReportRetrieverImpl(trelloGateway, englishInterpreter);
//        categoryService.createIndex();
    }

    @Test
    void getReports() {
        var reports = reportRetriever.getReports();

        Assertions.assertNotNull(reports);
    }
}