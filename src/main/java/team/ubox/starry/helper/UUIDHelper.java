package team.ubox.starry.helper;

import java.util.UUID;

public class UUIDHelper {
    private UUIDHelper(){};

    public static String UUIDToString(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static UUID stringToUUID(String uuidStr) {
        if(!checkParsable(uuidStr)) {
            throw new IllegalArgumentException("잘못된 아이디 형식입니다.");
        }

        String _lowTime = uuidStr.substring(0, 8);
        String _midTime = uuidStr.substring(8, 12);
        String _version = uuidStr.substring(12, 16);
        String _variant = uuidStr.substring(16, 20);
        String _node = uuidStr.substring(20, 32);

        return UUID.fromString(String.format("%s-%s-%s-%s-%s", _lowTime, _midTime, _version, _variant, _node));
    }

    public static Boolean checkParsable(String uuidStr) {
        if(uuidStr == null) {
            return false;
        }

        if(uuidStr.length() != 32) {
            return false;
        }

        return uuidStr.matches("[a-z0-9]{32}");
    }
}

