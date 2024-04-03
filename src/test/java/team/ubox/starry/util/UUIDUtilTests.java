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

    @Test
    void parsable() {
        UUID testUUID = UUID.randomUUID();
        String uuidStr = UUIDUtil.UUIDToString(testUUID);

        // Success
        Assertions.assertEquals(UUIDUtil.checkParsable(uuidStr), true);

        // Shorter than 32 (len: 27)
        Assertions.assertEquals(UUIDUtil.checkParsable("qwertyuiopasdfghjklzxcvbnm"), false);

        // With uppercase
        Assertions.assertEquals(UUIDUtil.checkParsable("D416798c0929428ca648fd03ea29a121"), false);

        // With special character
        Assertions.assertEquals(UUIDUtil.checkParsable("d416798c0929428ca648f^03ea29a121"), false);
    }
}
