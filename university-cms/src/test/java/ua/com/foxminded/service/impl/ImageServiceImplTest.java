package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.enums.Gender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceImplTest {

  private final String USER_PROFILE_DIR = "user/profile/dir/";
  private final String APPLICATION_IMAGE_DIRECTORY = "application/images/dir/";
    @InjectMocks
    private ImageServiceImpl imageService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    ReflectionTestUtils.setField(imageService, "userProfileDirPath", USER_PROFILE_DIR);
    ReflectionTestUtils.setField(
            imageService, "applicationImagesDirectory", APPLICATION_IMAGE_DIRECTORY);

    try {
      Files.createDirectories(Paths.get(USER_PROFILE_DIR));
      Files.createDirectories(Paths.get(APPLICATION_IMAGE_DIRECTORY));
    } catch (IOException e) {
      throw new RuntimeException("Error creating directories for test setup", e);
    }
  }

  @AfterEach
  void tearDown() throws IOException {
    deleteDirectory("user");
    deleteDirectory("application");
  }

  private void deleteDirectory(String directoryPath) throws IOException {
    Path path = Paths.get(directoryPath);
    try (Stream<Path> pathStream = Files.walk(path)) {
      pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
  }

  @Test
  void saveUserImage_Success() {
    String userRole = "STUDENT";
    Long userId = 1L;
    MultipartFile imageFile =
            new MockMultipartFile("test-image.png", "image.png", "image/png", new byte[10]);

    String imageName = imageService.saveUserImage(userRole, userId, imageFile);

    assertNotNull(imageName);
    assertTrue(Files.exists(Paths.get(USER_PROFILE_DIR + imageName)));
  }

  @Test
  void deleteUserImage_Success() throws IOException {
    String imageName = "test-image.png";
    Path imagePath = Paths.get(USER_PROFILE_DIR, imageName);
    Files.createFile(imagePath);

    imageService.deleteUserImage(imageName);

    assertFalse(Files.exists(imagePath));
  }

  @Test
  void getDefaultUserImage_Success() {
    Gender gender = Gender.FEMALE;
    String userRole = "TEACHER";

    String defaultImage = imageService.getDefaultIUserImage(gender, userRole);

    assertNotNull(defaultImage);
    assertEquals("teacher_female.png", defaultImage);
  }

  @Test
  void readImageAsBytes_ImageExists_Success() throws IOException {
    String imageName = "existing-image.png";
    Path imagePath = Paths.get(USER_PROFILE_DIR, imageName);
    Files.createFile(imagePath);

    byte[] imageBytes = imageService.readImageAsBytes(imageName);

    assertNotNull(imageBytes);
  }

  @Test
  void readImageAsBytes_ImageNotExists_ExceptionThrown() {
    String imageName = "non-existing-image.png";

    assertThrows(RuntimeException.class, () -> imageService.readImageAsBytes(imageName));
  }

  @Test
  void saveUserImage_ExceptionThrown() {
    String userRole = "STUDENT";
    Long userId = 1L;

    assertThrows(RuntimeException.class, () -> imageService.saveUserImage(userRole, userId, null));
  }

  @Test
  void getDefaultUserImage_ExceptionThrown() {
    String userRole = "USER_ROLE";
    assertThrows(RuntimeException.class, () -> imageService.getDefaultIUserImage(null, userRole));
  }

  @Test
  void readImageAsBytes_IOException_ExceptionThrown() {
    String imageName = "non-existing-image.png";
    assertThrows(RuntimeException.class, () -> imageService.readImageAsBytes(imageName));
  }
}
