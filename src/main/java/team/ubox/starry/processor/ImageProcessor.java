package team.ubox.starry.processor;

import org.springframework.stereotype.Component;
import team.ubox.starry.helper.FileHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class ImageProcessor {
    private final String EXT_PNG = "png", EXT_JPG = "jpg";

    public CustomImage createImageFromFile(File target) throws IOException {
        CustomImage image = new CustomImage();

        String extension = FileHelper.getExtension(target);
        validateExtension(extension);

        FileInputStream fileInputStream = new FileInputStream(target);
        image.setBufferedImage(ImageIO.read(fileInputStream));
        image.setExtension(extension);

        return image;
    }

    private void validateExtension(String extension) throws FileNotFoundException {
        if(extension == null) {
            throw new FileNotFoundException("확장자가 없습니다.");
        }
        if(!extension.equals(EXT_PNG) && !extension.equals(EXT_JPG)) {
            throw new FileNotFoundException("지원하지 않는 이미지 파일 형식입니다.");
        }
    }

    public byte[] imageToByteArray(CustomImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image.getBufferedImage(), image.getExtension(), byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return imageByteArray;
    }

    public void resizeImage(CustomImage image, Integer width, Integer height) {
        BufferedImage originalBufferedImage = image.getBufferedImage();
        width = Math.min(width, originalBufferedImage.getWidth());
        height = Math.min(height, originalBufferedImage.getHeight());

        int xCenter = originalBufferedImage.getWidth() / 2;
        int yCenter = originalBufferedImage.getHeight() / 2;
        int xOffset = width / 2;
        int yOffset = height / 2;

        BufferedImage resizedBufferedImage = originalBufferedImage.getSubimage(xCenter - xOffset, yCenter - yOffset, width, height);
        BufferedImage copiedBufferedImage = new BufferedImage(resizedBufferedImage.getWidth(),
                resizedBufferedImage.getHeight(), resizedBufferedImage.getType());

        Graphics graphics = copiedBufferedImage.createGraphics();
        graphics.drawImage(resizedBufferedImage, 0, 0, null);
        graphics.dispose();

        image.setBufferedImage(copiedBufferedImage);
    }

}
