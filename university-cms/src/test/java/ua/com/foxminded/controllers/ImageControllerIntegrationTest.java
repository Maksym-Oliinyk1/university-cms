package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
class ImageControllerIntegrationTest extends BaseIntegrationTest {

    private static byte[] getTestImage() {
        try {
            return Files.readAllBytes((TEST_IMAGE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserImage() throws Exception {
        String imageName = "student_male.png";
        byte[] imageBytes = getTestImage();

        mvc.perform(get("/showImages/{imageName}", imageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageBytes));
    }
}
