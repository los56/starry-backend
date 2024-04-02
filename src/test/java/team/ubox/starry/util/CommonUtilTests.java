package team.ubox.starry.util;

import org.junit.jupiter.api.Test;

public class CommonUtilTests {

    @Test
    public void generateRandomStringTest() {
        final int length = 32;
        for(int i = 0;i < 100;i++) {
            System.out.println(CommonUtil.generateRandomString(length));
        }
    }
}
