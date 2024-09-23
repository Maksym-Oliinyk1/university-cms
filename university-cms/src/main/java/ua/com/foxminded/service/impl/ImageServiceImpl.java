package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.service.ImageService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

  private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
  private static final String IMAGE_FOLDER = "application-images/";
  private static final String USER_FOLDER = "user-images/";
  private static final String DEFAULT_FEMALE_STUDENT_AVATAR = "student_female.png";
  private static final String DEFAULT_MALE_STUDENT_MALE_AVATAR = "student_male.png";
  private static final String DEFAULT_FEMALE_TEACHER_AVATAR = "teacher_female.png";
  private static final String DEFAULT_MALE_TEACHER_AVATAR = "teacher_male.png";
  private static final String DEFAULT_FEMALE_ADMIN_AVATAR = "admin_female.png";
  private static final String DEFAULT_MALE_ADMIN_AVATAR = "admin_male.png";
  private static final String DEFAULT_FEMALE_MAINTAINER_AVATAR = "maintainer_female.png";
  private static final String DEFAULT_MALE_MAINTAINER_AVATAR = "maintainer_male.png";
  private static final String FEMALE_GENDER = "FEMALE";
  private static final String MALE_GENDER = "MALE";
  private static final Map<String, String> DEFAULT_IMAGES = new HashMap<>();

  static {
    DEFAULT_IMAGES.put("ADMINISTRATOR" + FEMALE_GENDER, DEFAULT_FEMALE_ADMIN_AVATAR);
    DEFAULT_IMAGES.put("ADMINISTRATOR" + MALE_GENDER, DEFAULT_MALE_ADMIN_AVATAR);
    DEFAULT_IMAGES.put("MAINTAINER" + FEMALE_GENDER, DEFAULT_FEMALE_MAINTAINER_AVATAR);
    DEFAULT_IMAGES.put("MAINTAINER" + MALE_GENDER, DEFAULT_MALE_MAINTAINER_AVATAR);
    DEFAULT_IMAGES.put("TEACHER" + FEMALE_GENDER, DEFAULT_FEMALE_TEACHER_AVATAR);
    DEFAULT_IMAGES.put("TEACHER" + MALE_GENDER, DEFAULT_MALE_TEACHER_AVATAR);
    DEFAULT_IMAGES.put("STUDENT" + FEMALE_GENDER, DEFAULT_FEMALE_STUDENT_AVATAR);
    DEFAULT_IMAGES.put("STUDENT" + MALE_GENDER, DEFAULT_MALE_STUDENT_MALE_AVATAR);
  }

  private final S3Client s3Client;
  private final String s3BucketName;

  @Autowired
  public ImageServiceImpl(S3Client s3Client, @Value("${aws.s3.bucket-name}") String s3BucketName) {
    this.s3Client = s3Client;
    this.s3BucketName = s3BucketName;
  }

  @Override
  public String saveUserImage(String userRole, Long userId, MultipartFile imageFile) {
    try {
      byte[] bytes = imageFile.getBytes();
      String originalFilename = imageFile.getOriginalFilename();
      String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

      String imageName = generateUniqueImageName(userRole, fileExtension);
      String s3Key = USER_FOLDER + imageName;

      s3Client.putObject(
              PutObjectRequest.builder().bucket(s3BucketName).key(s3Key).build(),
              RequestBody.fromBytes(bytes));

      logger.info("Image for an user: {}", userId.toString(), " is saved");
      return imageName;
    } catch (IOException e) {
      throw new RuntimeException("Failed to save image", e);
    }
  }

  @Override
  public void deleteUserImage(String imageName) {
    try {
      String s3Key = USER_FOLDER + imageName;
      s3Client.deleteObject(DeleteObjectRequest.builder().bucket(s3BucketName).key(s3Key).build());
      logger.info("Image {},", imageName, " is deleted");
    } catch (S3Exception e) {
      throw new RuntimeException("Failed to delete image", e);
    }
  }

  @Override
  public String getDefaultIUserImage(Gender gender, String userRole) {
    return DEFAULT_IMAGES.get(userRole + gender.name());
  }

  @Override
  public byte[] readImageAsBytes(String imageName) {
    try {
      String userKey = USER_FOLDER + imageName;

      GetObjectRequest userGetObjectRequest =
              GetObjectRequest.builder().bucket(s3BucketName).key(userKey).build();

      try {
        ResponseBytes<GetObjectResponse> userObjectBytes =
                s3Client.getObjectAsBytes(userGetObjectRequest);
        return userObjectBytes.asByteArray();
      } catch (S3Exception ignored) {
      }

      String defaultKey = IMAGE_FOLDER + imageName;

      GetObjectRequest defaultGetObjectRequest =
              GetObjectRequest.builder().bucket(s3BucketName).key(defaultKey).build();

      ResponseBytes<GetObjectResponse> defaultObjectBytes =
              s3Client.getObjectAsBytes(defaultGetObjectRequest);
      return defaultObjectBytes.asByteArray();
    } catch (S3Exception e) {
      throw new RuntimeException("Failed to read image: " + imageName, e);
    }
  }

  private String generateUniqueImageName(String userRole, String fileExtension) {
    return String.format("%s_%s%s", UUID.randomUUID(), userRole, fileExtension);
  }
}
