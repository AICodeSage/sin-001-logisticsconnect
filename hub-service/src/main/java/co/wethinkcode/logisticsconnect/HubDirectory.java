package co.wethinkcode.logisticsconnect;

import java.io.IOException;
import java.util.List;

/** Keeps the hub API responsive while retaining ingestion-service as its source. */
public final class HubDirectory {
    private final IngestionClient ingestionClient;
    private volatile List<Hub> cached = List.of();

    public HubDirectory(IngestionClient ingestionClient) {
        this.ingestionClient = ingestionClient;
    }

    public List<Hub> all() throws IOException, InterruptedException {
        cached = ingestionClient.all();
        return cached;
    }

    public Hub find(String hubId) throws IOException, InterruptedException {
        return all().stream().filter(hub -> hub.hubId().equalsIgnoreCase(hubId)).findFirst().orElse(null);
    }
}
