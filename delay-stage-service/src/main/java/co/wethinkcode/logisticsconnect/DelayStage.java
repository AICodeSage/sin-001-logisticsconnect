package co.wethinkcode.logisticsconnect;

/** The disruption severity at a hub, constrained to the domain range 0 through 8. */
public record DelayStage(String hubId, int stage) {
    public DelayStage {
        if (hubId == null || hubId.isBlank()) throw new IllegalArgumentException("hubId is required");
        if (stage < 0 || stage > 8) throw new IllegalArgumentException("stage must be between 0 and 8");
    }
}
