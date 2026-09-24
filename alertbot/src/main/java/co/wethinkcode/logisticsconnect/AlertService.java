package co.wethinkcode.logisticsconnect;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Creates simulated public notices at a transparent severity threshold. */
public final class AlertService {
    public static final int ALERT_THRESHOLD = 5;
    private final CopyOnWriteArrayList<Alert> alerts = new CopyOnWriteArrayList<>();

    public void handle(DelayStageEvent event) {
        if (event.stage() < ALERT_THRESHOLD) return;
        Alert alert = new Alert(event.hubId(), event.stage(),
                "Service alert: " + event.hubId() + " is at delay stage " + event.stage(), Instant.now().toString());
        alerts.add(alert);
        System.out.println("SIMULATED SOCIAL POST: " + alert.message());
    }

    public List<Alert> all() {
        return List.copyOf(alerts);
    }
}
