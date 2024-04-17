package person.birch.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import person.birch.model.Report;
import person.birch.model.ReportsContext;
import person.birch.model.TrelloItem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ReportWriterImplTest {

    @Inject
    ReportWriter reportWriter;

    @Test
    void writeReport() {
        var trelloItem = new TrelloItem(List.of(new Report(null, "reports.items.starlink", "reports.descriptions.7-23-1", "21 000", "shared.currency.uah", "12.07.2023")));
        var descriptionUkr = Map.of("04-2024-1", "бронепластини");
        var descriptionEng = Map.of("04-2024-1", "armor plates");
        var reportsContext = new ReportsContext(descriptionUkr, descriptionEng, trelloItem);

        reportWriter.writeReport(reportsContext);
        Assertions.assertTrue(Files.exists(Path.of("output/report.json")));
        Assertions.assertTrue(Files.exists(Path.of("output/descriptionUkr.json")));
        Assertions.assertTrue(Files.exists(Path.of("output/descriptionEng.json")));
    }
}