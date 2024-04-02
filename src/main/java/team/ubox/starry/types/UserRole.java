package team.ubox.starry.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    USER("USER"),
    STREAMER("STREAMER"),
    ADMIN("ADMIN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @JsonCreator
    public static UserRole from(String value) {
        for(UserRole role : UserRole.values()) {
            if(role.getValue().equals(value)) {
                return role;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
