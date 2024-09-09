package ua.com.foxminded.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.enums.Gender;

public interface ImageService {
  String saveUserImage(String userRole, MultipartFile imageFile);

  void deleteUserImage(String imageName);

  String getDefaultIUserImage(Gender gender, String userRole);

  byte[] readImageAsBytes(String imageName);
}
