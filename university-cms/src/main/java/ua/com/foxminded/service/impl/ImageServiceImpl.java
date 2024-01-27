package ua.com.foxminded.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.User;
import ua.com.foxminded.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
    private static final String DEFAULT_PLACEHOLDER_AVATAR = "placeholder.png";
    private static final String FEMALE_GENDER = "FEMALE";
    private static final String MALE_GENDER = "MALE";
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


    public String saveUserImage(Long userId, MultipartFile imageFile) {
        deleteUserImage(userId);
        try {
            byte[] bytes = imageFile.getBytes();
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

            String imageName = userId + fileExtension;
            Path path = Paths.get(userProfileDirPath + imageName);

            Files.write(path, bytes);

            return imageName;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteUserImage(Long userId) {
        if (userId != null) {
            try (Stream<Path> filesStream = Files.list(Paths.get(userProfileDirPath))) {
                List<Path> files = filesStream
                        .filter(path -> path.getFileName().toString().startsWith(String.valueOf(userId)))
                        .toList();
                for (Path file : files) {
                    Files.deleteIfExists(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void setDefaultImageForUser(User user) {
        Map<String, String> defaultImages = new HashMap<>();
        defaultImages.put("Administrator" + FEMALE_GENDER, DEFAULT_FEMALE_ADMIN_AVATAR);
        defaultImages.put("Administrator" + MALE_GENDER, DEFAULT_MALE_ADMIN_AVATAR);
        defaultImages.put("Maintainer" + FEMALE_GENDER, DEFAULT_FEMALE_MAINTAINER_AVATAR);
        defaultImages.put("Maintainer" + MALE_GENDER, DEFAULT_MALE_MAINTAINER_AVATAR);
        defaultImages.put("Teacher" + FEMALE_GENDER, DEFAULT_FEMALE_TEACHER_AVATAR);
        defaultImages.put("Teacher" + MALE_GENDER, DEFAULT_MALE_TEACHER_AVATAR);
        defaultImages.put("Student" + FEMALE_GENDER, DEFAULT_FEMALE_STUDENT_AVATAR);
        defaultImages.put("Student" + MALE_GENDER, DEFAULT_MALE_STUDENT_MALE_AVATAR);

        String userType = user.getClass().getSimpleName();
        String gender = user.getGender().name();
        String imageName = defaultImages.get(userType + gender);

        if (imageName != null) {
            user.setImageName(imageName);
        }
    }

    @Override
    public String getImagePath(User user) {
        String imageName = user.getImageName();

        if (hasUserImage(imageName)) {
            return userProfileDirPath + imageName;
        }

        if (hasDefaultImage(imageName)) {
            return applicationImagesDirectory + imageName;
        }

        return applicationImagesDirectory + DEFAULT_PLACEHOLDER_AVATAR;
    }

    public boolean hasUserImage(String imageName) {
        return Files.exists(Paths.get(userProfileDirPath + imageName));
    }

    public boolean hasDefaultImage(String imageName) {
        return Files.exists(Paths.get(applicationImagesDirectory, imageName));
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

}

