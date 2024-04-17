package person.birch.service;

import person.birch.model.ReportsContext;

public interface ReportRetriever {
    ReportsContext getReports(String... args);
}
