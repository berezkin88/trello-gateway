package person.birch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.mutiny.Uni;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface TrelloGateway {

    Uni<String> getListDetails(String listId) throws URISyntaxException, JsonProcessingException;
    Uni<String> getCardBy(String id) throws URISyntaxException;
    Uni<String> getLists() throws URISyntaxException;
    File downloadImage(String imageUrl, String date) throws URISyntaxException, InterruptedException, IOException;
}
