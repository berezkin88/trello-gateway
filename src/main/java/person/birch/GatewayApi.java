package person.birch;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.mutiny.Uni;
import person.birch.service.TrelloGateway;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

@Path("")
public class GatewayApi {

    @Inject
    TrelloGateway trelloGateway;

    @Path("/lists/{listId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> getListById(@PathParam("listId") String listId) throws URISyntaxException, JsonProcessingException {
        return trelloGateway.getListDetails(listId);
    }

    @Path("/cards/{identification}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> getCardBy(@PathParam("identification") String identification) throws URISyntaxException {
        return trelloGateway.getCardBy(identification);
    }

    @Path("/lists")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> getLists() throws URISyntaxException {
        return trelloGateway.getLists();
    }
}