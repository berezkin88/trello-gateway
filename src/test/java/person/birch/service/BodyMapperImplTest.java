package person.birch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class BodyMapperImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(BodyMapperImplTest.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("Test Body Mapper")
    @Test
    void simplifyResponseBody() {
        try  {
            var example = new String(getClass().getResourceAsStream("/example.json").readAllBytes());
            var mapper = new BodyMapperImpl();

            var result = mapper.simplifyCardsResponseBody(example);
            LOG.info(result);
            assertNotNull(result);
        } catch (IOException e) {
            LOG.error("Error", e);
            fail();
        }
    }
}