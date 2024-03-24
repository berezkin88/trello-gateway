package person.birch.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3 s3Client;


    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public List<String> listObjects() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public void uploadFile(File file) throws IOException {
        s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), new FileInputStream(file), null));
    }

    public S3Object downloadFile(String fileName) {
        return s3Client.getObject(new GetObjectRequest(bucketName, fileName));
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
    public String getFileUrl(String fileName) {
        return s3Client.getUrl(bucketName, fileName).toExternalForm();
    }
}