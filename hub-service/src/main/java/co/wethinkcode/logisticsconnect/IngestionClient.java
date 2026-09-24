package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/** HTTP boundary for the cleaned hub source of truth. */
public final class IngestionClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public IngestionClient() {
        this(HttpClient.newHttpClient(), new ObjectMapper(), System.getenv().getOrDefault("INGESTION_URL", "http://localhost:7050"));
    }

    IngestionClient(HttpClient client, ObjectMapper mapper, String baseUrl) {
        this.client = client;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
    }

    public List<Hub> all() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/hubs");
        if (response.statusCode() != 200) throw new IOException("Ingestion service returned " + response.statusCode());
        return mapper.readValue(response.body(), new TypeReference<>() { });
    }

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(3)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
