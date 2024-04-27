package person.birch;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.service.ReportRetriever;
import person.birch.service.ReportWriter;

@QuarkusMain
public class App implements QuarkusApplication {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private final ReportWriter reportWriter;
    private final ReportRetriever reportRetriever;

    @Inject
    public App(ReportWriter reportWriter,
               ReportRetriever reportRetriever) {
        this.reportWriter = reportWriter;
        this.reportRetriever = reportRetriever;
    }

    public void getReports(String... args) {
        LOG.info("Створення звітів за {} місяць {} рік", args[0], args[1]);
        var reports = reportRetriever.getReports(args);
        LOG.info("Отримано {} звітів за вказаний період", reports.trelloItem().items().size());
        reportWriter.writeReport(reports);
        LOG.info("Звіти записано у директорію 'output'");

    }

    @Override
    public int run(String... args) {
        if (args.length == 0) {
            LOG.info("Період для звітів не вказаний");
            Quarkus.waitForExit();
            return 1;
        }
        getReports(args);
        Quarkus.waitForExit();
        return 0;
    }

    public static void main(String... args) {
        Quarkus.run(App.class, args);
    }
}
