package person.birch.service;

import io.smallrye.mutiny.Uni;
import person.birch.model.ReportsContainer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface EnglishInterpreter {
    ReportsContainer translateToEng(Uni<String> input) throws ExecutionException, InterruptedException, TimeoutException;
}
