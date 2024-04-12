package person.birch.category;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.List;

@Singleton
@ConfigProperties(prefix = "idx")
public class CategoriesConfig {
    private String location;
    private List<Category> categories;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
