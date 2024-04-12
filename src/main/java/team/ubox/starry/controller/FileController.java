package team.ubox.starry.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.ubox.starry.helper.FileHelper;
import team.ubox.starry.service.dto.CustomResponse;
import team.ubox.starry.service.dto.file.FileDTO;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.service.FileService;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService staticFileService;

    @PostMapping("/upload-image")
    public CustomResponse<FileDTO.ResponseImage> uploadProfileImage(@RequestParam MultipartFile uploadFile) {
        return new CustomResponse<>(staticFileService.uploadProfileImage(uploadFile));
    }


    @GetMapping("/image/{originalFileName}/{desireFileName}")
    public void downloadImage(@PathVariable String originalFileName, @PathVariable String desireFileName,
                              @RequestParam(required = false) String size, HttpServletResponse response) {
        byte[] imageByteArray = staticFileService.getImageByteArray(originalFileName, size);

        response.setContentType("image/" + FileHelper.getExtension(originalFileName));

        try {
            OutputStream responseOutputStream = response.getOutputStream();
            responseOutputStream.write(imageByteArray);
            responseOutputStream.close();
        } catch (IOException e) {
            throw new CustomException(CustomError.INTERNAL_SERVER_ERROR);
        }
    }
}
