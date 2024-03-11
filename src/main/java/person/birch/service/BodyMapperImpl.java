package person.birch.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;

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

    private static JSONObject getCardObject(JsonNode card) {
        var cardObject = new JSONObject();

        cardObject.put("id", card.get("id").textValue());
        cardObject.put("name", card.get("name").textValue());
        cardObject.put("desc", card.get("desc").textValue());
        cardObject.put("date", card.get("dateLastActivity").textValue());
        cardObject.put("url", card.get("shortUrl").textValue());
        return cardObject;
    }

    // todo: get 'cover' by size
    // todo: get 'dateLastActivity' by size
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

    private static JSONObject getAttachmentObject(JsonNode attachment) {
        var attachmentObject = new JSONObject();

        attachmentObject.put("id", attachment.get("id").textValue());
        attachmentObject.put("name", attachment.get("name").textValue());
        attachmentObject.put("type", attachment.get("mimeType").textValue());
        attachmentObject.put("downloadUrl", attachment.get("url").textValue());
        return attachmentObject;
    }
}
