package person.birch.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.model.Report;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Singleton
public class ImageConveyorImpl implements ImageConveyor {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConveyorImpl.class);
    private final TrelloGateway trelloGateway;
    private final S3Service s3Service;

    public ImageConveyorImpl(TrelloGateway trelloGateway, S3Service s3Service) {
        this.trelloGateway = trelloGateway;
        this.s3Service = s3Service;
    }

    @Override
    public void convey(List<Report> reports) {
        reports.forEach(report -> {
            if (null == report.getImage() || "n/a".equals(report.getImage())) {
                LOG.info("No image available. Skipping...");
                return;
            }
            try {
                var imageFile = trelloGateway.downloadImage(report.getImage(), report.getDate());
                s3Service.uploadFile(imageFile);
                var s3Url = s3Service.getUrl(imageFile.getName());
                report.setImage(s3Url);
            } catch (URISyntaxException | InterruptedException | IOException e) {
                LOG.error("Failed to download image from {}", report.getImage(), e);
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
