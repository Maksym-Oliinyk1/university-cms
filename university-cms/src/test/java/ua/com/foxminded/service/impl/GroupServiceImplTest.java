package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        Mockito.reset(groupRepository, studentRepository);
    }

    @Test
    void saveGroup_ValidName_Success() {
        Group group = new Group(1L, "AB-12");
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        groupService.save(group);

        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void saveGroup_InvalidName_ThrowsException() {
        Group group = new Group(1L, "Invalid123");
        assertThrows(RuntimeException.class, () -> groupService.save(group));

        verify(groupRepository, never()).save(group);
    }


    @Test
    void deleteGroup_Exists_Success() {
        Long id = 1L;
        when(groupRepository.existsById(id)).thenReturn(true);

        groupService.delete(id);

        verify(groupRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteGroup_NotExists_ThrowsException() {
        Long id = 1L;
        when(groupRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> groupService.delete(id));

        verify(groupRepository, never()).deleteById(id);
    }

    @Test
    void findByIdGroup_Exists_Success() {
        Long id = 1L;
        Group group = new Group(id, "AB-12");
        when(groupRepository.existsById(id)).thenReturn(true);
        when(groupRepository.findById(id)).thenReturn(Optional.of(group));

        Group result = groupService.findById(id);

        assertNotNull(result);
        assertEquals(group, result);
    }

    @Test
    void findByIdGroup_NotExists_ThrowsException() {
        Long id = 1L;
        when(groupRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> groupService.findById(id));
    }

    @Test
    void findAllGroups_Success() {
        Group group1 = new Group(0L, "AB-12");
        Group group2 = new Group(0L, "CD-34");
        when(groupRepository.findAll()).thenReturn(Arrays.asList(group1, group2));

        List<Group> result = groupService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(group1));
        assertTrue(result.contains(group2));
    }

    @Test
    void findAllGroupsToPage_Success() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1L, "AB-12"));
        groups.add(new Group(2L, "CD-32"));

        when(groupRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(groups));

        Pageable pageable = PageRequest.of(0, 10);
        groupService.findAll(pageable);

        verify(groupRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    void attachStudentToGroup_Success() {
        Long studentId = 1L;
        Long groupId = 2L;
        Group group = new Group(0L, "AB-12");
        Student student = new Student(0L, "John", "Doe", group);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        groupService.attachStudentToGroup(studentId, groupId);

        verify(groupRepository, times(1)).save(group);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void detachStudentFromGroup_Success() {
        Long studentId = 1L;
        Long groupId = 2L;
        Group group = new Group(0L, "AB-12");
        Student student = new Student(0L, "John", "Doe", group);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        groupService.detachStudentFromGroup(studentId, groupId);

        verify(groupRepository, times(1)).save(group);
        verify(studentRepository, times(1)).save(student);
    }



    @Test
    void showLecturesBetweenDates_ValidGroupIdAndDates_Success() {
        Long groupId = 1L;
        LocalDateTime firstDate = LocalDateTime.now();
        LocalDateTime secondDate = firstDate.plusDays(5);

        when(groupRepository.existsById(groupId)).thenReturn(true);
        when(groupRepository.countLecturesByGroup(groupId)).thenReturn(5L);
        when(groupRepository.findLecturesByDateBetween(eq(groupId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<Lecture> result = groupService.showLecturesBetweenDates(groupId, firstDate, secondDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void showLecturesBetweenDates_InvalidGroupId_ThrowsException() {
        Long groupId = 1L;
        LocalDateTime firstDate = LocalDateTime.now();
        LocalDateTime secondDate = firstDate.plusDays(5);

        when(groupRepository.existsById(groupId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> groupService.showLecturesBetweenDates(groupId, firstDate, secondDate));

        verify(groupRepository, never()).countLecturesByGroup(groupId);
        verify(groupRepository, never()).findLecturesByDateBetween(eq(groupId), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}