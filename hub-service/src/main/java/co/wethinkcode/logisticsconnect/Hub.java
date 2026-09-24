package co.wethinkcode.logisticsconnect;

/** Public hub representation received from ingestion-service. */
public record Hub(String hubId, String province, String sortingCenter, boolean active) {
}
