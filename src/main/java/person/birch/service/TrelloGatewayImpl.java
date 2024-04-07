package person.birch.service;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.config.CredConfig;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.text.MessageFormat.format;

// rebuild as command line app https://quarkus.io/guides/command-mode-reference
@Singleton
public class TrelloGatewayImpl implements TrelloGateway {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String HTTPS_TRELLO_URL = "https://api.trello.com/1";
    private final HttpClient client = HttpClient.newHttpClient();

    @Inject
    CredConfig credConfig;
    @Inject
    BodyMapper bodyMapper;

    @Override
    public Uni<String> getListDetails(String listId) throws URISyntaxException {
        log.debug("Get completed cards from list [{}]", listId);

        var uriString
            = format("{0}/lists/{1}/cards?key={2}&token={3}&attachments=true", HTTPS_TRELLO_URL, listId, credConfig.tKey(), credConfig.tToken());

        var request = HttpRequest.newBuilder()
            .uri(new URI(uriString))
            .GET()
            .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return Uni.createFrom().future(response).map(HttpResponse::body).map(bodyMapper::simplifyCardsResponseBody);
    }

    @Override
    public Uni<String> getCardBy(String cardId) throws URISyntaxException {
        log.debug("Get card [{}]", cardId);

        var urlString
            = format("{0}/cards/{1}?key={2}&token={3}&attachments=true", HTTPS_TRELLO_URL, cardId, credConfig.tKey(), credConfig.tToken());

        var request = HttpRequest.newBuilder()
            .uri(new URI(urlString))
            .GET()
            .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return Uni.createFrom().future(response).map(HttpResponse::body).map(bodyMapper::simplifyCardDetailsResponseBody);
    }

    public Uni<String> getLists() throws URISyntaxException {
        var board = credConfig.tBoardId();
        log.debug("Get all available lists from the board [{}]", board);

        var urlStr
            = format("{0}/boards/{1}/lists?key={2}&token={3}", HTTPS_TRELLO_URL, board, credConfig.tKey(), credConfig.tToken());

        var request = HttpRequest.newBuilder()
            .uri(new URI(urlStr))
            .GET()
            .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return Uni.createFrom().future(response).map(HttpResponse::body);
    }
}
