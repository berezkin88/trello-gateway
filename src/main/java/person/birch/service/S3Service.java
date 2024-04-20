package person.birch.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Singleton
public class S3Service {

    @ConfigProperty(name = "aws.s3.bucket")
    String bucketName;
    @Inject
    S3Client s3Client;

    public List<String> listObjects() {
        var listObjectsRequest = ListObjectsRequest.builder()
            .bucket(bucketName)
            .build();
        return s3Client.listObjects(listObjectsRequest)
                .contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }

    public void uploadFile(File file) {
        var putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(file.getName())
            .contentType("image/webp")
            .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    /**
     * Will safe a targeted file into 'output' folder
     * @param fileName of targeted file
     * @return GetObjectResponse
     */
    public GetObjectResponse downloadFile(String fileName) {
        var getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();
        return s3Client.getObject(getObjectRequest, Path.of("output/" + fileName));
    }

    public void deleteFile(String fileName) {
        var deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public String getUrl(String fileName) {
        var request = GetUrlRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();
        return s3Client.utilities().getUrl(request).toString();
    }
}