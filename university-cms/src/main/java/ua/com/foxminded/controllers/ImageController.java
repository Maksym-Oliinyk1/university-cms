package ua.com.foxminded.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.utill.UtilController;

@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/showImages/{imageName}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserImage(@PathVariable String imageName) {
        byte[] imageBytes = imageService.readImageAsBytes(imageName);

        String fileExtension = imageName.substring(imageName.lastIndexOf('.'));
        MediaType mediaType = UtilController.getMediaTypeForFileExtension(fileExtension);

        return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
    }
}
