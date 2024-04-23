package person.birch;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.service.ReportRetriever;
import person.birch.service.ReportWriter;

import java.util.Scanner;

// rebuild as command line app https://quarkus.io/guides/command-mode-reference
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
        LOG.info("Отримано {} за вказаний період", reports.trelloItem().items().size());
        reportWriter.writeReport(reports);
        LOG.info("Звіти записано у директорію 'output'");
    }

    @Override
    public int run(String... args) throws Exception {
        var scanner = new Scanner(System.in);
        LOG.info("""
            
            Вас вітає Менеджер Звітів!
            Будь ласка, введіть запит, за який період Ви хочете сформувати звіти.
            Формат запиту <місяць рік>. Зразок: серпень 2022
            """);

        boolean isFinished = false;

        var input = scanner.nextLine();
        while (!isFinished) {
            LOG.info(input);
            if (input.equalsIgnoreCase("exit")) {
                LOG.info("\nЗавершення роботи Менеджера");
                isFinished = true;
                continue;
            }
            if (input.isBlank() || input.isEmpty()) {
                LOG.info("""
                    
                    Запит незрозумілий.
                    Формат запиту <місяць рік>. Зразок: серпень 2022
                    """);
            }
            input = scanner.nextLine();
        }
        Quarkus.waitForExit();
        return 0;
    }
}
