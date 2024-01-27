package ua.com.foxminded.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.User;

public interface ImageService {
    String saveUserImage(Long userId, MultipartFile imageFile);

    void deleteUserImage(Long userId);

    void setDefaultImageForUser(User user);

    String getImagePath(User user);

    byte[] readImageAsBytes(String imageName);
}
