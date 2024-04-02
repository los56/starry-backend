package team.ubox.starry.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UUIDUtilTests {

    @Test
    void allTest() {
        UUID testUUID = UUID.randomUUID();
        String hexUUID = UUIDUtil.UUIDToString(testUUID);
        Assertions.assertEquals(hexUUID.length(), 32);

        UUID makeUUID = UUIDUtil.stringToUUID(hexUUID);
        Assertions.assertEquals(testUUID, makeUUID);
    }
}
