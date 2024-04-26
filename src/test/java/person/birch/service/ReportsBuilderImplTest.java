package person.birch.service;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

class ReportsBuilderImplTest {

    @Test
    void buildReports() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        var reportsBuilder = new ReportsBuilderImpl(null);

        var resourceAsStream = getClass().getResourceAsStream("/cleaned-body.json");
        var example = new String(Objects.requireNonNull(resourceAsStream).readAllBytes());

        var uni = Uni.createFrom().item(example);

        var actual = reportsBuilder.buildReports(uni, "лютий 2022");

        System.out.println(actual);
        Assertions.assertNotNull(actual);
    }
}