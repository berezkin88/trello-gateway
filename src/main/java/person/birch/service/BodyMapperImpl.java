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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

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
        cardObject.put("description", getDescription(card.get("name")));
        cardObject.put("price", getPrice(card.get("name")));
        cardObject.put("currency", "shared.currency.uah");
        cardObject.put("date", getDate(card.get("dateLastActivity")));
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

    private String getDescription(JsonNode nameNode) {
        if (null == nameNode) {
            return "n/a";
        }

        var nameString = nameNode.textValue();
        var cutDescription = nameString.substring(0, nameString.indexOf("("));

        return cutDescription.trim();
    }

    private String getPrice(JsonNode nameNode) {
        if (null == nameNode) {
            return "n/a";
        }

        var nameString = nameNode.textValue();
        var pattern = Pattern.compile("\\((.+)[KКkк]\\)"); // Grab everything between parentheses. Latin 'Kk' and Cyrillic 'Кк' are considered
        var matcher = pattern.matcher(nameString);

        if (!matcher.find()) {
            return "n/a";
        }

        var price = matcher.group(1)
            .replace(",", "."); // just in case if a comma slips in.

        var number = new BigDecimal(price).multiply(new BigDecimal(1000));
        return String.format("%,d", number.intValue());
    }

    private String getDate(JsonNode dateNode) {
        if (null == dateNode) {
            return "n/a";
        }

        var textDateTime = dateNode.textValue();
        try {
            var dateTime = OffsetDateTime.parse(textDateTime);
            return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            LOG.error("Failed to parse date, [{}]", textDateTime);
            return "n/a";
        }
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
