package person.birch.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BodyMapperImpl implements BodyMapper {

    private static final Logger LOG = LoggerFactory.getLogger(BodyMapperImpl.class);
    private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    @Override
    public String simplifyCardsResponseBody(String source) {
        try {
            var originalNodes = mapper.readTree(source);

            var cardsArray = new JSONArray();

            for (var card : originalNodes) {
                cardsArray.put(getCardObject(card));
            }

            return cardsArray.toString();
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse message", e);
            return "";
        }
    }

    private JSONObject getCardObject(JsonNode card) {
        var cardObject = new JSONObject();

        cardObject.put("image", getCover(card.get("cover")));
        cardObject.put("item", "reports.items.medicine"); //todo: add classification
        cardObject.put("description", card.get("name").textValue());
        cardObject.put("price", card.get("name").textValue()); //todo: cut price from the name
        cardObject.put("currency", "shared.currency.uah");
        cardObject.put("date", card.get("dateLastActivity").textValue());
        return cardObject;
    }

    private String getCover(JsonNode coverNode) {
        if (null == coverNode) {
            return "n/a";
        }

        var scaleNodes = coverNode.get("scaled");
        if (null == scaleNodes || scaleNodes.isEmpty()) {
            return "n/a";
        }

        for (var scaleNode : scaleNodes) {
            var width = scaleNode.get("width").asInt();
            if (599 <= width) {
                return scaleNode.get("url").textValue();
            }
        }

        return "n/a";
    }

    @Override
    public String simplifyCardDetailsResponseBody(String source) {
        try {
            var originalNodes = mapper.readTree(source);

            var cardObject = getCardObject(originalNodes);

            var attachments = originalNodes.get("attachments");
            var attachmentsArray = new JSONArray();

            for (var attachment : attachments) {
                attachmentsArray.put(getAttachmentObject(attachment));
            }

            cardObject.put("attachments", attachmentsArray);

            return cardObject.toString();
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse message", e);
            return "";
        }
    }

    private JSONObject getAttachmentObject(JsonNode attachment) {
        var attachmentObject = new JSONObject();

        attachmentObject.put("id", attachment.get("id").textValue());
        attachmentObject.put("name", attachment.get("name").textValue());
        attachmentObject.put("type", attachment.get("mimeType").textValue());
        attachmentObject.put("downloadUrl", attachment.get("url").textValue());
        return attachmentObject;
    }
}
