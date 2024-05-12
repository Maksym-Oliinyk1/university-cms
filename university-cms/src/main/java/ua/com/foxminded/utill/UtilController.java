package ua.com.foxminded.utill;

import org.springframework.http.MediaType;

public class UtilController {

    public static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    public static MediaType getMediaTypeForFileExtension(String fileExtension) {
        fileExtension = fileExtension.toLowerCase();
        return switch (fileExtension) {
            case ".jpg", ".jpeg" -> MediaType.IMAGE_JPEG;
            case ".png" -> MediaType.IMAGE_PNG;
            default -> throw new RuntimeException("Unsupported file extension: " + fileExtension);
        };
    }

    public static String extractToken(String authToken) {
        if (authToken != null && authToken.startsWith("Bearer ")) {
            return authToken.substring(7);
        }
        return authToken;
    }
}
