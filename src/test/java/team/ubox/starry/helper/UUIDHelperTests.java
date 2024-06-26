package team.ubox.starry.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UUIDHelperTests {

    @Test
    void allTest() {
        UUID testUUID = UUID.randomUUID();
        String hexUUID = UUIDHelper.UUIDToString(testUUID);
        Assertions.assertEquals(hexUUID.length(), 32);

        UUID makeUUID = UUIDHelper.stringToUUID(hexUUID);
        Assertions.assertEquals(testUUID, makeUUID);
    }

    @Test
    void parsable() {
        UUID testUUID = UUID.randomUUID();
        String uuidStr = UUIDHelper.UUIDToString(testUUID);

        // Success
        Assertions.assertEquals(true, UUIDHelper.checkParsable(uuidStr));

        // Shorter than 32 (len: 27)
        Assertions.assertEquals(false, UUIDHelper.checkParsable("qwertyuiopasdfghjklzxcvbnm"));

        // With uppercase
        Assertions.assertEquals(false, UUIDHelper.checkParsable("D416798c0929428ca648fd03ea29a121"));

        // With special character
        Assertions.assertEquals(false, UUIDHelper.checkParsable("d416798c0929428ca648f^03ea29a121"));
    }
}
