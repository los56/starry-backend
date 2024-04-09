package team.ubox.starry.helper;

import java.io.File;

public class FileHelper {
    private FileHelper() {}

    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex == -1) {
            return null;
        }
        return fileName.substring(dotIndex + 1);
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }
}
