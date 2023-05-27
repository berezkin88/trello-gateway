package person.birch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.mutiny.Uni;

import java.net.URISyntaxException;

public interface TrelloGateway {

    Uni<String> getListDetails(String listId) throws URISyntaxException, JsonProcessingException;
    Uni<String> getCardBy(String id) throws URISyntaxException;
    Uni<String> getLists() throws URISyntaxException;
}
