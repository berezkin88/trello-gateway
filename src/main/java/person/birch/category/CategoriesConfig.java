package person.birch.category;

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
        public String getTitle();
        @WithName("keywords")
        public String getKeywords();
    }
}
