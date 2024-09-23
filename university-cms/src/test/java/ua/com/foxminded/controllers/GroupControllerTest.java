package ua.com.foxminded.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.controllers.group.GroupController;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.StudentService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(GroupController.class)
class GroupControllerTest extends BaseSecurityTestClass {

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;
  @MockBean
  private GroupService groupService;
  @MockBean
  private LectureService lectureService;
  @MockBean
  private StudentService studentService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  void showGroup_ValidId_ShouldReturnGroupPage() throws Exception {
    configureSecurity();

    Long groupId = 1L;
    Group mockGroup = new Group();

    when(groupService.findById(groupId)).thenReturn(mockGroup);

    mockMvc
            .perform(get("/general/group/showGroup").param("id", String.valueOf(groupId)))
            .andExpect(status().isOk())
            .andExpect(view().name("group"))
            .andExpect(model().attributeExists("group"))
            .andExpect(model().attribute("group", mockGroup));

    verify(groupService, times(1)).findById(groupId);
  }

  @Test
  void listGroups_ValidPageNumber_ShouldReturnManageGroupPage() throws Exception {
    configureSecurity();

    Page<Group> mockPageGroups =
            new PageImpl<>(
                    List.of(new Group()),
                    PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                    1);

    when(groupService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
            .thenReturn(mockPageGroups);

    mockMvc
            .perform(
                    get("/general/group/listGroups")
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("manage-group"))
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("groups", mockPageGroups.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageGroups.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageGroups.getTotalPages()));

    verify(groupService, times(1))
            .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
  }

  @Test
  void listGroupsToStudent_ValidPageNumber_ShouldReturnCreateFormStudentPage() throws Exception {
    configureSecurity();

    Page<Group> mockPageGroups =
            new PageImpl<>(
                    List.of(new Group()),
                    PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY),
                    1);

    when(groupService.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY)))
            .thenReturn(mockPageGroups);

    mockMvc
            .perform(
                    get("/general/group/listGroupsToStudent")
                            .param("pageNumber", String.valueOf(DEFAULT_PAGE_NUMBER)))
            .andExpect(status().isOk())
            .andExpect(view().name("create-form-student"))
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("pageNumber"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attribute("groups", mockPageGroups.getContent()))
            .andExpect(model().attribute("pageNumber", mockPageGroups.getNumber()))
            .andExpect(model().attribute("totalPages", mockPageGroups.getTotalPages()));

    verify(groupService, times(1))
            .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
  }
}
