package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.image.ImageController;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.utill.UtilController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ImageControllerTest extends BaseSecurityTestClass {
  private static final Path TEST_IMAGE_PATH = Path.of("src/test/resources/images/student_male.png");
  private static final byte[] TEST_IMAGE = getTestImage();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ImageService imageService;

  private static byte[] getTestImage() {
    try {
      return Files.readAllBytes((TEST_IMAGE_PATH));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testGetUserImage() throws Exception {
    String imageName = "student_male.png";

    String fileExtension = imageName.substring(imageName.lastIndexOf('.'));
    MediaType mediaType = UtilController.getMediaTypeForFileExtension(fileExtension);

    configureSecurity();

    when(imageService.readImageAsBytes(imageName)).thenReturn(TEST_IMAGE);

    mockMvc
            .perform(
                    get("/showImages/{imageName}", imageName).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(mediaType))
            .andExpect(content().bytes(TEST_IMAGE));
  }
}
