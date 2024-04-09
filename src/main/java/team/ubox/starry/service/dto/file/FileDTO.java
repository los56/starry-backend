package team.ubox.starry.service.dto.file;

import lombok.*;

public class FileDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class ResponseImage {
        private String fileName;
        private String originalFileName;
    }
}
