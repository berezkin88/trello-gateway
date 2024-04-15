package person.birch.service;

import person.birch.model.ReportsContainer;

public interface ReportRetriever {
    ReportsContainer getReports(String... args);
}
