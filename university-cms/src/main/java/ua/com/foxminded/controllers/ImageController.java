package ua.com.foxminded.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.foxminded.service.ImageService;

@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/showImages/{imageName}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes = imageService.readImageAsBytes(imageName);

            String fileExtension = imageName.substring(imageName.lastIndexOf('.'));
            MediaType mediaType = getMediaTypeForFileExtension(fileExtension);

            return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType getMediaTypeForFileExtension(String fileExtension) {
        return switch (fileExtension.toLowerCase()) {
            case ".jpg", ".jpeg" -> MediaType.IMAGE_JPEG;
            case ".png" -> MediaType.IMAGE_PNG;
            default -> throw new RuntimeException("Unsupported file extension: " + fileExtension);
        };
    }
}
