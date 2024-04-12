package person.birch.service;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Singleton
public class GTranslatorService {
    private static final String USER_AGENT = "User-Agent";
    private static final String MOZILLA_5_0 = "Mozilla/5.0";
    @ConfigProperty(name = "g-translator.url")
    String url;

    /**
     * Translate text from ukrainian to english
     * @param text string in ukrainian
     * @return english text
     */
    public String translate(String text) throws IOException, URISyntaxException {
        String encodedQuery = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String urlStr = url + "?q=" + encodedQuery + "&target=en&source=uk";
        var completeUrl = new URI(urlStr).toURL();
        var response = new StringBuilder();
        var con = (HttpURLConnection) completeUrl.openConnection();
        con.setRequestProperty(USER_AGENT, MOZILLA_5_0);
        var in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
