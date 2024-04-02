package team.ubox.starry.util;

import java.util.UUID;

public class UUIDUtil {
    private UUIDUtil(){};

    public static String UUIDToString(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static UUID stringToUUID(String uuidStr) {
        String _lowTime = uuidStr.substring(0, 8);
        String _midTime = uuidStr.substring(8, 12);
        String _version = uuidStr.substring(12, 16);
        String _variant = uuidStr.substring(16, 20);
        String _node = uuidStr.substring(20, 32);

        return UUID.fromString(String.format("%s-%s-%s-%s-%s", _lowTime, _midTime, _version, _variant, _node));
    }
}

