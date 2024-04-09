package team.ubox.starry.processor;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class StarryImage {
    private BufferedImage bufferedImage;
    private String extension;
}