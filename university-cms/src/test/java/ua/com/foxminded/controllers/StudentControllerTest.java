package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.student.StudentController;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @MockBean
    private StudentService studentService;

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void studentAuthorization_ShouldReturnStudentAuthorizationPage() throws Exception {
        mockMvc.perform(get("/studentAuthorization"))
                .andExpect(status().isOk())
                .andExpect(view().name("mock-student-authorization"));
    }

    @Test
    void manageStudent_ShouldReturnManageStudentPage() throws Exception {
        mockMvc.perform(get("/manageStudent"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-student"));
    }

    @Test
    void showStudent_ValidId_ShouldReturnStudentPage() throws Exception {
        Long studentId = 1L;
        Student mockStudent = createMockStudentWithGroup();
        when(studentService.findById(studentId)).thenReturn(mockStudent);

        mockMvc.perform(get("/showStudent")
                        .param("id", String.valueOf(studentId)))
                .andExpect(status().isOk())
                .andExpect(view().name("student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", mockStudent));

        verify(studentService, times(1)).findById(studentId);
    }

    @Test
    void listStudents_ShouldReturnManageStudentPage() throws Exception {
        Page<Student> mockStudentPage = mock(Page.class);
        when(mockStudentPage.getContent()).thenReturn(Collections.emptyList());
        when(studentService.findAll(any())).thenReturn(mockStudentPage);

        mockMvc.perform(get("/listStudents"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-student"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(studentService, times(1)).findAll(any());
    }

    @Test
    void listStudentsByGroup_ValidGroupId_ShouldReturnGroupPageWithStudents() throws Exception {
        Long groupId = 1L;
        Group mockGroup = new Group();
        Page<Student> mockStudentPage = mock(Page.class);
        when(mockStudentPage.getContent()).thenReturn(Collections.emptyList());
        when(groupService.findById(groupId)).thenReturn(mockGroup);
        when(studentService.findAllStudentByGroup(groupId, PageRequest.of(0, 10))).thenReturn(mockStudentPage);

        mockMvc.perform(get("/listStudentsByGroup/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"));

        verify(groupService, times(1)).findById(groupId);
        verify(studentService, times(1)).findAllStudentByGroup(groupId, PageRequest.of(0, 10));
    }

    @Test
    void createStudent_ValidInput_ShouldReturnCreateFormStudentSuccessfulPage() throws Exception {
        mockMvc.perform(post("/createStudent")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("gender", "MALE")
                        .param("birthDate", "1990-01-01")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-form-student-successful"));

        verify(studentService, times(1)).save(any());
    }

    @Test
    void createStudent_InvalidInput_ShouldReturnCreateFormStudentPageWithErrors() throws Exception {
        mockMvc.perform(post("/createStudent"))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).save(any());
    }

    @Test
    void showUpdateForm_ValidId_ShouldReturnUpdateFormStudentPage() throws Exception {
        Long studentId = 1L;
        StudentDTO mockStudentDTO = new StudentDTO();

        when(studentService.findByIdDTO(studentId)).thenReturn(mockStudentDTO);

        mockMvc.perform(get("/updateFormStudent/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", mockStudentDTO));

        verify(studentService, times(1)).findByIdDTO(studentId);
    }

    @Test
    void updateStudent_ValidInput_ShouldReturnUpdateFormStudentSuccessfulPage() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(post("/updateStudent/{id}", studentId)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("gender", "MALE")
                        .param("birthDate", "1990-01-01")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-form-student-successful"));

        verify(studentService, times(1)).update(eq(studentId), any());
    }

    @Test
    void updateStudent_InvalidInput_ShouldReturnUpdateFormStudentPageWithErrors() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(post("/updateStudent/{id}", studentId))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).update(eq(studentId), any());
    }

    @Test
    void deleteStudent_ValidId_ShouldReturnDeleteFormStudentSuccessfulPage() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(post("/deleteStudent/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-form-student-successful"));

        verify(studentService, times(1)).delete(studentId);
    }

    private Student createMockStudentWithGroup() {
        Student mockStudent = new Student();
        Group mockGroup = new Group();
        mockGroup.setId(1L);
        mockStudent.setGroup(mockGroup);
        return mockStudent;
    }
}
