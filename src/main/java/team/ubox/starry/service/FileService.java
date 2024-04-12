package team.ubox.starry.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.helper.AuthHelper;
import team.ubox.starry.helper.FileHelper;
import team.ubox.starry.processor.ImageProcessor;
import team.ubox.starry.processor.CustomImage;
import team.ubox.starry.repository.StaticFileRepository;
import team.ubox.starry.repository.entity.StaticFile;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.service.dto.file.FileDTO;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final StaticFileRepository staticFileRepository;
    private final ImageProcessor imageProcessor;

    @Getter
    @Value("${starry.static-file.store}")
    private String storePath;

    public FileDTO.ResponseImage uploadProfileImage(MultipartFile multipartFile) {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));

        String ip = getIp();

        if(multipartFile.getSize() > (1024 * 1024 * 10L)) {
            throw new CustomException(CustomError.EXCEED_FILE_SIZE);
        }

        String originalFileName = multipartFile.getOriginalFilename();
        if(originalFileName == null) {
            throw new CustomException(CustomError.NOT_ALLOW_FILE_TYPE);
        }

        String ext = FileHelper.getExtension(originalFileName);
        if(ext == null || (!ext.equals("png") && !ext.equals("jpg"))) {
            throw new CustomException(CustomError.NOT_ALLOW_FILE_TYPE);
        }

        String generatedName = UUID.randomUUID() + "." + ext;

        File newFile = new File(storePath + generatedName);
        try {
            multipartFile.transferTo(newFile);
        } catch (IOException e) {
            throw new CustomException(CustomError.INTERNAL_SERVER_ERROR);
        }

        StaticFile staticFile = staticFileRepository.save(StaticFile.builder()
                                                                    .fileName(generatedName)
                                                                    .originalFileName(originalFileName)
                                                                    .uploader(user.getId())
                                                                    .uploaderIp(ip)
                                                                    .uploadDate(Timestamp.from(Instant.now()))
                                                                    .build());

        return FileDTO.ResponseImage.builder()
                .fileName(String.format("%s/%s", generatedName, originalFileName))
                .build();
    }

    private String getIp() {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = httpRequest.getHeader("X-FORWARDED-FOR");
        if(ip == null) {
            ip = httpRequest.getRemoteAddr();
        }

        return ip;
    }

    public byte[] getImageByteArray(String fileName, String size) {
        File file = new File(storePath + "/" + fileName);

        CustomImage image;
        try {
            image = imageProcessor.createImageFromFile(file);
        } catch (IOException e) {
            throw new CustomException(CustomError.FILE_NOT_FOUND);
        }

        try {
            if(size != null) {
                Size desireSize = parseSize(size);
                imageProcessor.resizeImage(image, desireSize.width, desireSize.height);
            }
        } catch (IllegalArgumentException e) {
            // 사이즈 파싱에 실패한 경우 원본을 내보내므로 아무 동작도 하지 않음
        }

        byte[] imageByteArray;
        try {
            imageByteArray = imageProcessor.imageToByteArray(image);
        } catch (IOException e) {
            throw new CustomException(CustomError.INTERNAL_SERVER_ERROR);
        }


        return imageByteArray;
    }

    private Size parseSize(String size) throws IllegalArgumentException {
        String[] splitsByX = size.split("x");

        if(splitsByX.length < 2) {
            throw new IllegalArgumentException("size가 잘못되었습니다.");
        }

        int width = Integer.parseInt(splitsByX[0]);
        int height = Integer.parseInt(splitsByX[1]);

        return new Size(width, height);
    }
}

class Size {
    public int width;
    public int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}