package person.birch.config;

import io.smallrye.config.ConfigMapping;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3ClientConfig {

    @Inject
    S3Config s3Config;

    @Singleton
    public S3Client s3client() {

        var awsCredentials = AwsBasicCredentials.create(s3Config.accessKey(), s3Config.secretKey());

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(s3Config.region()))
                .build();
    }

    @ConfigMapping(prefix = "aws.s3")
    interface S3Config {
        String accessKey();
        String secretKey();
        String bucket();
        String region();
    }
}