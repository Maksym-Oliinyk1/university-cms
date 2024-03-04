package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
class TeacherControllerIntegrationTest extends BaseIntegrationTest {


    @Test
    void teacherAuthorization() throws Exception {
        mvc.perform(get("/teacherAuthorization"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("mock-teacher-authorization"));
    }

    @Test
    void manageTeacher() throws Exception {
        mvc.perform(get("/manageTeacher"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-teacher"));
    }

    @Test
    void showTeacher() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mvc.perform(get("/showTeacher?id=1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("teacher"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    void listTeachers() throws Exception {
        mvc.perform(get("/listTeachers"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("manage-teacher"));
    }

    @Test
    void createFormTeacher() throws Exception {
        mvc.perform(get("/createFormTeacher"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-teacher"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    void createTeacher_successful() throws Exception {
        TeacherDTO teacherDTO = createTeacherDTO();

        mvc.perform(post("/createTeacher")
                        .flashAttr("teacherDTO", teacherDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-form-teacher-successful"));
    }

    @Test
    void showUpdateForm() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mvc.perform(get("/updateFormTeacher/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-teacher"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    void updateTeacher_successful() throws Exception {
        String updatedTeacherName = "UpdatedTeacher";
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setFirstName(updatedTeacherName);

        mvc.perform(post("/updateTeacher/1")
                        .flashAttr("teacherDTO", teacherDTO))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("update-form-teacher-successful"));

        Optional<Teacher> optionalTeacher = teacherRepository.findById(1L);
        assertTrue(optionalTeacher.isPresent());
        Teacher updatedTeacher = optionalTeacher.get();
        assertEquals(updatedTeacher.getFirstName(), updatedTeacherName);
    }

    @Test
    void deleteTeacher_successful() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mvc.perform(post("/deleteTeacher/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("delete-form-teacher-successful"));

        assertFalse(teacherRepository.findById(1L).isPresent());
    }
}
