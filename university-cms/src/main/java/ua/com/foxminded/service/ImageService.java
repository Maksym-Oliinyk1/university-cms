package ua.com.foxminded.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.User;
import ua.com.foxminded.enums.Gender;

public interface ImageService {
    String saveUserImage(String userRole, Long userId, MultipartFile imageFile);

    void deleteUserImage(String imageName);

    String getDefaultIUserImage(Gender gender, String userRole);

    String getImagePath(User user);

    byte[] readImageAsBytes(String imageName);
}
