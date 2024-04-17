package person.birch;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.birch.service.ReportRetriever;
import person.birch.service.ReportWriter;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AppTest {

    private App app;
    @Inject
    ReportRetriever reportRetriever;
    @Inject
    ReportWriter reportWriter;

    @BeforeEach
    void setUp() {
        app = new App(reportWriter, reportRetriever);
    }

    @Test
    void getReports() {
        app.getReports("березень", "2024");
        Assertions.assertTrue(Files.exists(Path.of("output/report.json")));
        Assertions.assertTrue(Files.exists(Path.of("output/descriptionUkr.json")));
        Assertions.assertTrue(Files.exists(Path.of("output/descriptionEng.json")));
    }
}