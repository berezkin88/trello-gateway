package person.birch.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "source")
public interface CredConfig {

    String tKey();
    String tToken();
    String tBoardId();
}
