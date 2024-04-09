package team.ubox.starry.helper;

import java.util.Random;

public class StringHelper {
    private StringHelper() {};

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
