package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
class LectureControllerIntegrationTest extends BaseIntegrationTest {


    @Test
    void listLectures() throws Exception {
        MvcResult result = mvc.perform(get("/listLectures"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-lecture"))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        assertTrue(model.containsKey("lectures"));
        assertTrue(model.containsKey("pageNumber"));
        assertTrue(model.containsKey("totalPages"));

        List<Lecture> lectures = (List<Lecture>) model.get("lectures");
        int pageNumber = (int) model.get("pageNumber");
        int totalPages = (int) model.get("totalPages");
        assertFalse(lectures.isEmpty());
        assertEquals(0, pageNumber);
        assertEquals(4, totalPages);
    }

    @Test
    void showLecture() throws Exception {
        Lecture lecture = createLecture();
        lectureRepository.save(lecture);

        mvc.perform(get("/showLecture?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("lecture"))
                .andExpect(model().attributeExists("lecture"));
    }

    @Test
    void listLecturesByCourseId() throws Exception {
        Course course = createCourse();
        courseRepository.save(course);

        mvc.perform(get("/listLecturesByCourse/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("course"))
                .andExpect(model().attributeExists("lectures", "pageNumber", "totalPages", "course"));
    }

    @Test
    void listLecturesByGroupId() throws Exception {
        Group group = createGroup();
        groupRepository.save(group);

        mvc.perform(get("/listLecturesByGroup/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group", "lectures", "pageNumber", "totalPages"));
    }

    @Test
    void createFormLecture() throws Exception {
        mvc.perform(get("/createFormLecture"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-lecture"))
                .andExpect(model().attributeExists("lecture"));
    }

    @Test
    void createLecture_successful() throws Exception {
        Lecture lecture = createLecture();

        mvc.perform(post("/createLecture")
                        .flashAttr("lecture", lecture))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-lecture-successful"));

        Optional<Lecture> optionalSavedLecture = lectureRepository.findById(1L);
        assertTrue(optionalSavedLecture.isPresent());
        Lecture lectureFromModel = optionalSavedLecture.get();
        assertEquals("Introduction to Programming", lectureFromModel.getName());
        assertEquals("Test.Introduction to Programming.Test.Test.Introduction to Programming.Test.", lectureFromModel.getDescription());
        assertEquals(LocalDateTime.of(2025, 4, 23, 8, 30, 0), lectureFromModel.getDate());
    }

    @Test
    void showUpdateForm() throws Exception {
        Lecture lecture = createLecture();
        lectureRepository.save(lecture);

        mvc.perform(get("/updateFormLecture/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-lecture"))
                .andExpect(model().attributeExists("lecture"));
    }

    @Test
    void updateLecture_successful() throws Exception {
        String updatedLectureName = "Updatedlecture";
        Lecture lectureToSave = createLecture();
        lectureRepository.save(lectureToSave);

        Lecture lecture = createLecture();
        lecture.setName(updatedLectureName);

        mvc.perform(post("/updateLecture/1")
                        .flashAttr("lecture", lecture))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-lecture-successful"));

        Optional<Lecture> optionalLecture = lectureRepository.findById(1L);
        assertTrue(optionalLecture.isPresent());
        assertEquals(optionalLecture.get().getName(), updatedLectureName);
    }

    @Test
    void deleteLecture_successful() throws Exception {
        Lecture lecture = createLecture();
        lectureRepository.save(lecture);

        mvc.perform(post("/deleteLecture/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-lecture-successful"));

        assertFalse(lectureRepository.findById(1L).isPresent());
    }

}
