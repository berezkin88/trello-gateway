package person.birch.category;

import io.smallrye.config.WithName;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties
public class CategoryImpl implements CategoriesConfig.Category {
    private String key;
    private String title;
    private String keywords;

    @WithName("key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @WithName("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @WithName("keywords")
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
