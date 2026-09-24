package co.wethinkcode.logisticsconnect;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Loads the bundled export once and resolves duplicate physical locations. */
public final class HubRepository {
    private final List<HubRecord> hubs;

    public HubRepository() {
        hubs = load();
    }

    public List<HubRecord> all() {
        return hubs;
    }

    public HubRecord findById(String hubId) {
        String normalizedId = HubCleaner.hubId(hubId);
        return hubs.stream().filter(hub -> hub.hubId().equals(normalizedId)).findFirst().orElse(null);
    }

    private List<HubRecord> load() {
        Map<String, HubRecord> uniqueLocations = new LinkedHashMap<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                requireResource(), StandardCharsets.UTF_8))) {
            reader.readNext();
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length < 4) continue;
                String id = HubCleaner.hubId(row[0]);
                String province = HubCleaner.placeName(row[1]);
                String center = HubCleaner.placeName(row[2]);
                var active = HubCleaner.active(row[3]);
                if (id.isEmpty() || province.isEmpty() || center.isEmpty() || active.isEmpty()) continue;
                HubRecord candidate = new HubRecord(id, province, center, active.get());
                String locationKey = (province + "|" + center).toLowerCase();
                uniqueLocations.merge(locationKey, candidate, HubRepository::preferredRecord);
            }
        } catch (IOException | CsvValidationException exception) {
            throw new IllegalStateException("Could not load hubs-global.csv", exception);
        }
        return uniqueLocations.values().stream().sorted(Comparator.comparing(HubRecord::hubId)).toList();
    }

    private java.io.InputStream requireResource() {
        var stream = getClass().getResourceAsStream("/hubs-global.csv");
        if (stream == null) throw new IllegalStateException("hubs-global.csv is missing from the application");
        return stream;
    }

    // One canonical row per physical location: prefer an active record, then the lowest stable ID.
    private static HubRecord preferredRecord(HubRecord left, HubRecord right) {
        if (left.active() != right.active()) return left.active() ? left : right;
        return left.hubId().compareTo(right.hubId()) <= 0 ? left : right;
    }
}
