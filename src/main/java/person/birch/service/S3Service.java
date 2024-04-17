package person.birch.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

//@Singleton // skip for now
public class S3Service {

    @ConfigProperty(name = "aws.s3.bucket")
    String bucketName;
//    @Inject
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

    public void uploadFile(File file) throws IOException {
        var putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(file.getName())
            .contentType("application/json")
            .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    public String downloadFile(String fileName) {
        var getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();
        return s3Client.getObjectAsBytes(getObjectRequest)
            .asString(Charset.defaultCharset());
    }

    public void deleteFile(String fileName) {
        var deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}