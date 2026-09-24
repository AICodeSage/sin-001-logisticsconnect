package co.wethinkcode.logisticsconnect;

import java.util.concurrent.ConcurrentHashMap;

/** Materialized view of delay-stage topic events, keyed by normalized hub ID. */
public final class DelayStageCache {
    private final ConcurrentHashMap<String, Integer> stages = new ConcurrentHashMap<>();

    public void update(DelayStageEvent event) {
        if (event != null && event.hubId() != null && event.stage() >= 0 && event.stage() <= 8) {
            stages.put(event.hubId().trim().toUpperCase(), event.stage());
        }
    }

    public int stageFor(String hubId) {
        return stages.getOrDefault(hubId.trim().toUpperCase(), 0);
    }
}
