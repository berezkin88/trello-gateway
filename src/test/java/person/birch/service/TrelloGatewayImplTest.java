package person.birch.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

@QuarkusTest
class TrelloGatewayImplTest {

    @Inject
    TrelloGateway gateway;

    @Test
    void downloadImage() throws URISyntaxException, InterruptedException, IOException {
        var testUrl = "https://trello.com/1/cards/65295dbbbae253fc9f12281c/attachments/6568582bab977c222b306f98/previews/6568582cab977c222b30716e/download/helmets.jpg";

        var actual = gateway.downloadImage(testUrl, "30.11.2023");

        Assertions.assertTrue(actual.exists());
        actual.deleteOnExit();
    }
}