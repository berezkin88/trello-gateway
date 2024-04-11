package person.birch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GTranslatorService {
    private static final String USER_AGENT = "User-Agent";
    private static final String MOZILLA_5_0 = "Mozilla/5.0";
    @Value("${g-translator.url}")
    private String url;

    /**
     * Translate text from ukrainian to english
     * @param text string in ukrainian
     * @return english text
     * @throws IOException
     */
    public String translate(String text) throws IOException {
        String encodedQuery = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String urlStr = url + "?q=" + encodedQuery + "&target=en&source=uk";
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty(USER_AGENT, MOZILLA_5_0);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
