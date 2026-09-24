package co.wethinkcode.logisticsconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HubCleanerTest {
    @Test
    void normalizesIdsPlacesAndFlags() {
        assertEquals("H-501", HubCleaner.hubId(" h-501 "));
        assertEquals("Cape Town Port", HubCleaner.placeName("Cape Town  PORT"));
        assertEquals("KwaZulu-Natal", HubCleaner.placeName("kwa-zulu natal"));
        assertTrue(HubCleaner.active("YES").orElseThrow());
    }
}
