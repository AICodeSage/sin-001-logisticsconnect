package co.wethinkcode.logisticsconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class DelayStageStoreTest {
    @Test
    void defaultsToZeroAndStoresAValidUpdate() {
        DelayStageStore store = new DelayStageStore();
        assertEquals(0, store.get("h-501").stage());
        assertEquals(3, store.set("h-501", 3).stage());
        assertEquals("H-501", store.get("H-501").hubId());
    }

    @Test
    void rejectsStagesOutsideTheDomainRange() {
        assertThrows(IllegalArgumentException.class, () -> new DelayStage("H-1", 9));
    }
}
