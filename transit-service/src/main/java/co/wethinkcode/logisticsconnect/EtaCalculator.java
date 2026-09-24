package co.wethinkcode.logisticsconnect;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/** Maps each delay stage to a simple, documented ETA penalty. */
public final class EtaCalculator {
    private static final int BASE_MINUTES = 60;
    private static final int MINUTES_PER_STAGE = 30;

    public Eta calculate(Hub hub, int stage) {
        int minutes = BASE_MINUTES + stage * MINUTES_PER_STAGE;
        Instant arrival = Instant.now().plus(minutes, ChronoUnit.MINUTES);
        return new Eta(hub.hubId(), hub.sortingCenter(), stage, minutes, arrival.toString());
    }
}
