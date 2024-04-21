package person.birch;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.service.ReportRetriever;
import person.birch.service.ReportWriter;

// rebuild as command line app https://quarkus.io/guides/command-mode-reference
@ApplicationScoped
public class App {

    public static final Logger LOG = LoggerFactory.getLogger(App.class);

    private final ReportWriter reportWriter;
    private final ReportRetriever reportRetriever;

    public App(ReportWriter reportWriter, ReportRetriever reportRetriever) {
        this.reportWriter = reportWriter;
        this.reportRetriever = reportRetriever;
    }

    public void getReports(String... args) {
        LOG.info("Створення звітів за {} місяць {} рік", args[0], args[1]);
        var reports = reportRetriever.getReports(args);
        LOG.info("Отримано {} за вказаний період", reports.trelloItem().items().size());
        reportWriter.writeReport(reports);
        LOG.info("Звіти записано у директорію 'output'");
    }
}
