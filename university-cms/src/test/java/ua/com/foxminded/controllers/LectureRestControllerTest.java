package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(LectureRestController.class)
class LectureRestControllerTest {

    @MockBean
    private LectureService lectureService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getLectures_ShouldReturnListOfLectures() throws Exception {
        List<Lecture> mockLectures =
                Arrays.asList(createLecture(1L, "Lecture 1"), createLecture(2L, "Lecture 2"));

        Page<Lecture> mockLecturePage = new PageImpl<>(mockLectures);

        when(lectureService.findAll(any(PageRequest.class))).thenReturn(mockLecturePage);

        mockMvc
                .perform(get("/getListLectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Lecture 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Lecture 2")));

        verify(lectureService, times(1)).findAll(any(PageRequest.class));
    }

    private Lecture createLecture(Long id, String name) {
        Lecture lecture = new Lecture();
        lecture.setId(id);
        lecture.setName(name);
        return lecture;
    }
}
