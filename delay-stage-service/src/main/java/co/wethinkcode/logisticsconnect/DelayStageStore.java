package co.wethinkcode.logisticsconnect;

import java.util.concurrent.ConcurrentHashMap;

/** In-memory state store; can be replaced by a persistent store without changing the API. */
public final class DelayStageStore {
    private final ConcurrentHashMap<String, Integer> stages = new ConcurrentHashMap<>();

    public DelayStage get(String hubId) {
        return new DelayStage(normalizeId(hubId), stages.getOrDefault(normalizeId(hubId), 0));
    }

    public DelayStage set(String hubId, int stage) {
        String id = normalizeId(hubId);
        DelayStage value = new DelayStage(id, stage);
        stages.put(id, stage);
        return value;
    }

    private static String normalizeId(String hubId) {
        return hubId == null ? "" : hubId.trim().toUpperCase();
    }
}
