package person.birch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.Files;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@QuarkusTest
class TrelloGatewayImplTest {

    @Inject
    TrelloGateway gateway;

    @Test
    void downloadImage() throws URISyntaxException, InterruptedException, IOException {
        var testUrl = "https://trello.com/1/cards/64d6a2a0e9774d74809172c6/attachments/64feeed45b1fe10a6138f634/previews/64feeed55b1fe10a6138f692/download/prof1group.ua_images_products_files_2771_9b78da33daa8a57d33520ee2ff4e9c78ca848a8b-500x500.webp";

        var actual = gateway.downloadImage(testUrl, "30.09.2023");

        Assertions.assertTrue(actual.exists());
        Files.copy(actual, new File("src/test/resources/" + actual.getName()));
        actual.deleteOnExit();
    }

    @Test
    void getLists() throws URISyntaxException, ExecutionException, InterruptedException {
        var actual = gateway.getLists()
            .subscribeAsCompletionStage()
            .get();

        Assertions.assertNotNull(actual);
        System.out.println(actual);
    }

    @Test
    void getListDetails() throws URISyntaxException, JsonProcessingException, ExecutionException, InterruptedException {
        var actual = gateway.getListDetails("64f1caf9c989fb8fb098de94")
            .subscribeAsCompletionStage()
            .get();

        Assertions.assertNotNull(actual);
        System.out.println(actual);
    }
}