package person.birch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.birch.model.ReportsContext;

import java.io.FileWriter;
import java.io.IOException;

@Singleton
public class ReportWriterImpl implements ReportWriter {

    public static final Logger LOG = LoggerFactory.getLogger(ReportWriterImpl.class);
    private final ObjectMapper objectMapper;

    public ReportWriterImpl() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Override
    public void writeReport(ReportsContext report) {
        try (
            var reportWriter = new FileWriter("output/report.json");
            var descriptionUkrWriter = new FileWriter("output/descriptionUkr.json");
            var descriptionEngWriter = new FileWriter("output/descriptionEng.json");
        ) {
            reportWriter.write(objectMapper.writeValueAsString(report.trelloItem()));
            descriptionUkrWriter.write(objectMapper.writeValueAsString(report.descriptionUkr()));
            descriptionEngWriter.write(objectMapper.writeValueAsString(report.descriptionEng()));
        } catch (IOException e) {
            LOG.error("Fail to write the file", e);
        }
    }
}
