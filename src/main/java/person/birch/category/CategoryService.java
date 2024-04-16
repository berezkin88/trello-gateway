package person.birch.category;

import jakarta.inject.Singleton;
import org.apache.lucene.analysis.uk.UkrainianMorfologikAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import person.birch.config.CategoriesConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

@Singleton
public class CategoryService {
    private static final String KEYWORDS_FIELD = "KEYWORDS";
    private static final String KEY_FIELD = "KEY";
    private static final String TITLE_FIELD = "TITLE";
    private final CategoriesConfig config;
    private final Directory directory;


    public CategoryService(CategoriesConfig config) throws IOException {
        this.config = config;
        directory = FSDirectory.open(Paths.get(config.location()));
    }

    /**
     * Run it when category index doesn't exist
     * or category was added or deleted
     * or when some properties of category were changed
     */
    public void createIndex() throws IOException {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig())) {
            var categories = config.categories();
            for (var category : categories) {
                Document doc = createDoc(category);
                writer.addDocument(doc);
            }
            writer.flush();
            writer.commit();
        }
    }


    public Optional<CategoryImpl> search(String searchString) throws IOException, ParseException {
        Query query = new QueryParser(KEYWORDS_FIELD, new UkrainianMorfologikAnalyzer()).parse(searchString);
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs topDocs = searcher.search(query, 1);

        if (topDocs.scoreDocs.length < 1) {
            return Optional.empty();
        }
        ScoreDoc scoreDoc = Arrays.stream(topDocs.scoreDocs).findFirst().get();
        Document doc = searcher.doc(scoreDoc.doc);
        CategoryImpl categoryImpl = createCategory(doc);

        return Optional.of(categoryImpl);
    }

    private CategoryImpl createCategory(Document doc) {
        CategoryImpl categoryImpl = new CategoryImpl();
        categoryImpl.setKey(doc.get(KEY_FIELD));
        categoryImpl.setTitle(doc.get(TITLE_FIELD));
        categoryImpl.setKeywords(doc.get(KEYWORDS_FIELD));
        return categoryImpl;
    }

    private Document createDoc(CategoriesConfig.Category category) {
        var doc = new Document();
        var key = new StringField(KEY_FIELD, category.getKey(), Field.Store.YES);
        var title = new StringField(TITLE_FIELD, category.getTitle(), Field.Store.YES);
        var keywords = new TextField(KEYWORDS_FIELD, category.getKeywords(), Field.Store.YES);

        doc.add(key);
        doc.add(title);
        doc.add(keywords);

        return doc;
    }
}
