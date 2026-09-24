package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** Synchronous lookup of location details from hub-service. */
public final class HubClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = System.getenv().getOrDefault("HUB_SERVICE_URL", "http://localhost:7051");

    public Hub find(String hubId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/hubs/" + hubId))
                .timeout(Duration.ofSeconds(3)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) return null;
        if (response.statusCode() != 200) throw new IOException("Hub service returned " + response.statusCode());
        return mapper.readValue(response.body(), Hub.class);
    }
}
