package team.ubox.starry.exception;

import lombok.Getter;

@Getter
public enum StarryError {
    INVALID_TOKEN("INVALID_TOKEN", "잘못된 인증 토큰입니다.", 401),
    NEED_TOKEN("NEED_TOKEN", "인증 토큰이 필요합니다.", 401),
    FORBIDDEN("FORBIDDEN", "권한이 없습니다", 403),
    REQUIRE_PARAMETER("REQUIRE_PARAMETER", "%s 값이 없습니다.", 400),
    INVALID_BODY("INVALID_BODY", "%s 은(는) %s", 400),

    // USER
    NOT_FOUND_USER("NOT_FOUND_USER", "존재하지 않는 사용자입니다.",400),
    BLANK_USERNAME("BLANK_USERNAME", "아이디 값이 비어있습니다.", 400),
    TOO_LONG_USERNAME("TOO_LONG_USERNAME","아이디가 너무 깁니다.", 400),
    TOO_SHORT_USERNAME("TOO_SHORT_USERNAME", "아이디가 너무 짧습니다.", 400),
    INVALID_USERNAME("INVALID_USERNAME", "아이디에 사용할 수 없는 글자가 포함되어 있습니다.", 400),
    BLANK_PASSWORD("BLANK_PASSWORD", "비밀번호 값이 비어있습니다.", 400),
    TOO_LONG_PASSWORD("TOO_LONG_PASSWORD", "비밀번호가 너무 깁니다.", 400),
    TOO_SHORT_PASSWORD("TOO_SHORT_PASSWORD", "비밀번호가 너무 짧습니다.", 400),
    INVALID_PASSWORD("INVALID_PASSWORD", "사용할 수 없는 비밀번호입니다.", 400),

    // CHANNEL
    BLANK_CHANNEL_ID("BLANK_CHANNEL_ID", "채널 아이디 값이 비어있습니다.", 400),
    INVALID_CHANNEL_ID("INVALID_CHANNEL_ID", "채널 아이디 형식이 잘못되었습니다.", 400),
    ALREADY_OPENED_CHANNEL("ALREADY_OPENED_CHANNEL", "이미 개설된 채널입니다.", 400),
    NOT_FOUND_CHANNEL("NOT_FOUND_CHANNEL", "존재하지 않는 채널입니다.", 404),
    NOT_FOUND_STREAM("NOT_FOUND_STREAM", "존재하지 않는 스트림입니다.", 404),
    INVALID_STREAM_KEY("INVALID_STREAM_KET", "잘못된 스트림 키입니다.", 401),
    ALREADY_FOLLOWED_CHANNEL("ALREADY_FOLLOWED_CHANNEL", "이미 팔로우 중입니다.", 400),
    NOT_FOLLOWED_CHANNEL("NOT_FOLLOWED_CHANNEL", "팔로우 중이 아닙니다.", 400),

    // COMMON
    NEGATIVE_INDEX("NEGATIVE_INDEX", "인덱스 값이 음수입니다.", 400)

    ;

    private final String errorType;
    private final String message;
    private final Integer defaultStatus;
    StarryError(String errorType, String message, Integer defaultStatus) {
        this.errorType = errorType;
        this.message = message;
        this.defaultStatus = defaultStatus;
    }
}
