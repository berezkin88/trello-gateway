package person.birch.model;

import java.util.List;

public record ReportsContainer(List<Report> ukrReports, List<Report> engReports) {
    void addUkrReport(Report ukrReport) {
        if (null == ukrReport) {
            return;
        }
        ukrReports.add(ukrReport);
    }

    void addEngReport(Report engReport) {
        if (null == engReport) {
            return;
        }
        engReports.add(engReport);
    }
}
