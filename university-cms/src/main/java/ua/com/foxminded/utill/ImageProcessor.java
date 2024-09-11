package ua.com.foxminded.utill;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageProcessor {
    private static final String IMAGE_FOLDER_PATH = "static/images/";

    public static String saveImage(Long userId, MultipartFile imageFile) {
        try {
            byte[] bytes = imageFile.getBytes();
            Path path =
                    Paths.get(IMAGE_FOLDER_PATH + userId.toString() + imageFile.getOriginalFilename());
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void deleteImage(String imagePath) {
        if (imagePath != null) {
            String fullPath = IMAGE_FOLDER_PATH + imagePath;
            try {
                Path of = Path.of(fullPath);
                Files.deleteIfExists(of);
                Files.deleteIfExists(of.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
