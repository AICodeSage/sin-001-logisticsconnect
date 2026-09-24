package co.wethinkcode.logisticsconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AlertServiceTest {
    @Test
    void onlyCreatesAlertsAtOrAboveThreshold() {
        AlertService service = new AlertService();
        service.handle(new DelayStageEvent("H-501", 4, "now"));
        service.handle(new DelayStageEvent("H-501", 5, "now"));
        assertEquals(1, service.all().size());
        assertEquals(5, service.all().get(0).stage());
    }
}
