package person.birch.model;

public record TrelloList(String id,
                         String name,
                         String closed,
                         String color,
                         String idBoard,
                         String pos,
                         String subscribed,
                         String softLimit) { }
