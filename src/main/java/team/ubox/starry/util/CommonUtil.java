package team.ubox.starry.util;

import java.util.Random;

public class CommonUtil {
    private CommonUtil() {};

    public static String generateRandomString(int length) {
        final int leftLimit = '0';
        final int rightLimit = 'z';
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= '9' || i >= 'A') && (i <= 'Z' || i >= 'a'))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
