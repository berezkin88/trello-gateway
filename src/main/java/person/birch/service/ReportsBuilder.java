package person.birch.service;

import io.smallrye.mutiny.Uni;
import person.birch.model.ReportsContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ReportsBuilder {
    ReportsContext buildReports(Uni<String> input) throws ExecutionException, InterruptedException, TimeoutException;
}
