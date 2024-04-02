package team.ubox.starry.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StreamStatus {
    LIVE("LIVE"), CLOSE("CLOSE");

    private final String value;

    StreamStatus(String value) { this.value = value; }

    @JsonCreator
    public static StreamStatus from(String value) {
        for(StreamStatus status : StreamStatus.values()) {
            if(status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
