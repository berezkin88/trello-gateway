package person.birch.category;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CategoryServiceTest {

    @Inject
    CategoryService categoryService;

    @Test
    void search() throws IOException, ParseException {
        categoryService.createIndex();
        var result = categoryService.search("берці");

        result.ifPresent(
            System.out::println
        );
        Assertions.assertNotNull(result.get());
    }
}