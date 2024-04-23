package person.birch.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

@Disabled("better not to run in pipeline, sequence is important")
@QuarkusTest
class S3ServiceTest {

    @Inject
    S3Service s3Service;

    @Test
    void uploadFile() {
        var file = Paths.get("src/test/resources/test-img.png").toFile();

        Assertions.assertTrue(file.exists());

        s3Service.uploadFile(file);

        var actual = s3Service.listObjects();

        System.out.println(actual);
        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
    }

    @Test
    void listObjects() {
        var actual = s3Service.listObjects();

        System.out.println(actual);
        Assertions.assertNotNull(actual);
    }

    @Test
    void downloadFile() {
        var fileName = "test-img.png";

        var actual = s3Service.downloadFile(fileName);

        Assertions.assertNotNull(actual);
    }

    @Test
    void getUrl() {
        var fileName = "test-img.png";

        var actual = s3Service.getUrl(fileName);

        Assertions.assertNotNull(actual);
        System.out.println(actual);
    }

    @Test
    void deleteFile() {
        var fileName = "test-img.png";

         s3Service.deleteFile(fileName);

        var actual = s3Service.listObjects();

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
    }
}