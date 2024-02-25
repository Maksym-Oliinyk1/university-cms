package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LectureRestControllerTest {

    @Mock
    private LectureService lectureService;

    @InjectMocks
    private LectureRestController lectureRestController;

    private MockMvc mockMvc;

    @Test
    void getLectures_ShouldReturnListOfLectures() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureRestController).build();

        List<Lecture> mockLectures = new ArrayList<>();
        Lecture lectureOne = new Lecture();
        Lecture lectureTwo = new Lecture();
        lectureOne.setId(1L);
        lectureOne.setName("Lecture 1");
        lectureTwo.setId(2L);
        lectureTwo.setName("Lecture 2");
        mockLectures.add(lectureOne);
        mockLectures.add(lectureTwo);

        Page<Lecture> mockLecturePage = new PageImpl<>(mockLectures);

        when(lectureService.findAll(any(PageRequest.class))).thenReturn(mockLecturePage);

        mockMvc.perform(get("/getListLectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Lecture 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Lecture 2")));

        verify(lectureService, times(1)).findAll(any(PageRequest.class));
    }
}