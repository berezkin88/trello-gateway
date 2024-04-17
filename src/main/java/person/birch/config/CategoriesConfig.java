package person.birch.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.List;

@ConfigMapping(prefix = "idx")
public interface CategoriesConfig {
    String location();
    List<Category> categories();

    interface Category {
        @WithName("key")
        String getKey();
        @WithName("title")
        String getTitle();
        @WithName("keywords")
        String getKeywords();
    }
}
