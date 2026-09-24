package co.wethinkcode.logisticsconnect;

public record Eta(String hubId, String sortingCenter, int delayStage, int estimatedMinutes, String arrivalWindow) {
}
