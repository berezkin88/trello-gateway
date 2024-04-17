package person.birch.model;

public class Report {
    private String image;
    private String item;
    private String description;
    private String price;
    private String currency;
    private String date;

    public Report() { } // for ObjectMapper

    public Report(String image, String item, String description, String price, String currency, String date) {
        this.image = image;
        this.item = item;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
