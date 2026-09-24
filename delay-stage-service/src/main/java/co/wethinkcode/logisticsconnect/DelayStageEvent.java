package co.wethinkcode.logisticsconnect;

/** Event emitted whenever a hub's delay stage changes. */
public record DelayStageEvent(String hubId, int stage, String timestamp) {
}
