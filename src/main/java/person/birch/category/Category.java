package person.birch.category;

public class Category {
    private String key;
    private String title;
    private String keywords;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Category{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
