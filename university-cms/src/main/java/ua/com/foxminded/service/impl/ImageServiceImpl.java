package ua.com.foxminded.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

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

    private final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    @Value("${app.image.storage.userProfileDirectory}")
    private String userProfileDirPath;
    @Value("${app.image.storage.applicationImagesDirectory}")
    private String applicationImagesDirectory;

    @PostConstruct
    public void init() {
        try {
            Path directoryPath = Paths.get(applicationImagesDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Resource[] resources = resourcePatternResolver.getResources("classpath:/static/images/*.*");
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    String filename = Paths.get(resource.getURI()).getFileName().toString();
                    Path destinationFile = directoryPath.resolve(filename);

                    if (!Files.exists(destinationFile)) {
                        try (InputStream inputStream = resource.getInputStream()) {
                            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage with default images", e);
        }
    }

    @Override
    public String saveUserImage(String userRole, Long userId, MultipartFile imageFile) {
        try {
            byte[] bytes = imageFile.getBytes();
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

            String imageName = generateUniqueImageName(userRole, fileExtension);
            Path path = Paths.get(userProfileDirPath + imageName);

            Files.write(path, bytes);

            return imageName;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteUserImage(String imageName) {
        if (imageName != null) {
            Path imagePath = Paths.get(userProfileDirPath, imageName);
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getDefaultIUserImage(Gender gender, String userRole) {
        return DEFAULT_IMAGES.get(userRole + gender.name());
    }

    @Override
    public byte[] readImageAsBytes(String imageName) {
        Path imagePath = determineImagePath(imageName);
        if (Files.exists(imagePath)) {
            try {
                return Files.readAllBytes(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Error reading image file: " + imageName, e);
            }
        } else {
            throw new RuntimeException("Image not found with name: " + imageName);
        }
    }

    private Path determineImagePath(String imageName) {
        Path path = Paths.get(userProfileDirPath, imageName);
        if (Files.exists(path)) {
            return path;
        }
        path = Paths.get(applicationImagesDirectory, imageName);
        if (Files.exists(path)) {
            return path;
        }
        throw new RuntimeException("Image not found with name: " + imageName);
    }

    private String generateUniqueImageName(String userRole, String fileExtension) {
        String imageName;
        Path path;

        do {
            imageName = String.format("%s_%s.%s", UUID.randomUUID(), userRole, fileExtension);
            path = Paths.get(userProfileDirPath, imageName);
        } while (Files.exists(path));

        return imageName;
    }
}

