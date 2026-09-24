package co.wethinkcode.logisticsconnect;

/** A cleaned row from the legacy hub export. */
public record HubRecord(String hubId, String province, String sortingCenter, boolean active) {
}
