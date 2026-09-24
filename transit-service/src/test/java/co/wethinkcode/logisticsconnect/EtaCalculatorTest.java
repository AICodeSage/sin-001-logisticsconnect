package co.wethinkcode.logisticsconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class EtaCalculatorTest {
    @Test
    void addsThirtyMinutesForEveryDelayStage() {
        Hub hub = new Hub("H-501", "Western Cape", "Cape Town Port", true);
        Eta eta = new EtaCalculator().calculate(hub, 3);
        assertEquals(150, eta.estimatedMinutes());
        assertEquals(3, eta.delayStage());
    }

    @Test
    void cacheUsesZeroUntilAnEventArrives() {
        DelayStageCache cache = new DelayStageCache();
        assertEquals(0, cache.stageFor("H-501"));
        cache.update(new DelayStageEvent("H-501", 4, "2026-01-01T00:00:00Z"));
        assertEquals(4, cache.stageFor("h-501"));
    }
}
