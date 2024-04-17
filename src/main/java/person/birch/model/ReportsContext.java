package person.birch.model;

import java.util.Map;

public record ReportsContext(Map<String, String> descriptionUkr,
                             Map<String, String> descriptionEng,
                             TrelloItem trelloItem) { }
