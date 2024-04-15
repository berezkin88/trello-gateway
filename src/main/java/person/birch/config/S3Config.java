package person.birch.config;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@ConfigProperties(prefix = "aws.s3")
public class S3Config {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;

    @Singleton
    public S3Client s3client() {

        var awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build();
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}