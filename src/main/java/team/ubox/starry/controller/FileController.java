package team.ubox.starry.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.ubox.starry.helper.FileHelper;
import team.ubox.starry.service.dto.StarryResponse;
import team.ubox.starry.service.dto.file.FileDTO;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.service.FileService;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService staticFileService;

    @PostMapping("/upload-image")
    public StarryResponse<FileDTO.ResponseImage> uploadProfileImage(@RequestParam MultipartFile uploadFile) {
        return new StarryResponse<>(staticFileService.uploadProfileImage(uploadFile));
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
            throw new StarryException(StarryError.INTERNAL_SERVER_ERROR);
        }
    }
}
