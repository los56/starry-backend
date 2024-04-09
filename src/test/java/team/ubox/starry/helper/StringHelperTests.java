package team.ubox.starry.helper;

import org.junit.jupiter.api.Test;

public class StringHelperTests {

    @Test
    public void generateRandomStringTest() {
        final int length = 32;
        for(int i = 0;i < 100;i++) {
            System.out.println(StringHelper.generateRandomString(length));
        }
    }
}
