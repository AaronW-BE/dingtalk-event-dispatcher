package vip.fastgo.event.dispatcher.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * TODO add signature validation for any handle request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class Subscriber extends DingtalkEventHandler {
    private String eventType;
    private String url;
    private String token;

    boolean handle(Map<?, ?> content, String plainText) {
        log.info("calling url: {}, pass content plain msg {}", url, plainText);
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(plainText)).uri(URI.create(url)).build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.body().contains("success")) {
                log.info("handle msg success");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            log.info("handle msg error, {}", e.getMessage());
        }
        return false;
    }

    /**
     * validate the subscriber url
     * @return true if the url is valid
     */
    public boolean validate() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create(url + "?tokekn=" + token)).build();
        try {
            log.info("validating subscriber url: {}", url);

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && StringUtils.hasText(response.body()) && response.body().contains("success")) {
                log.info("subscriber url: {} is valid", url);
                return true;
            }
        } catch (IOException | InterruptedException e) {
            log.error("failed to validate subscriber url: {}", url);
        }
        return false;
    }
}
