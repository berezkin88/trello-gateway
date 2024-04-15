package person.birch.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.model.ReportsContainer;

import java.net.URISyntaxException;

@Singleton
public class ReportRetrieverImpl implements ReportRetriever {

    public static final Logger LOG = LoggerFactory.getLogger(ReportRetrieverImpl.class);
    private final TrelloGateway trelloGateway;

    public ReportRetrieverImpl(TrelloGateway trelloGateway) {
        this.trelloGateway = trelloGateway;
    }

    @Override
    public ReportsContainer getReports(String... args) {

        try {
            var lists = trelloGateway.getLists();
            LOG.info(lists.toString());
        } catch (URISyntaxException e) {
            LOG.error("Failed to retrieve Lists from the Board", e);
            return null;
        }

        return null;
    }
}
