package ua.com.foxminded.controllers.integration;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ua.com.foxminded.dto.*;
import ua.com.foxminded.entity.*;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
@Rollback
public abstract class BaseIntegrationTest {

    protected static final Long DEFAULT_ID = 1L;

    protected static final Path TEST_IMAGE_PATH =
            Path.of("src/test/resources/images/student_male.png");

    @Autowired
    protected GroupRepository groupRepository;
    @Autowired
    protected LectureRepository lectureRepository;
    @Autowired
    protected StudentRepository studentRepository;
    @Autowired
    protected TeacherRepository teacherRepository;
    @Autowired
    protected CourseRepository courseRepository;
    @Autowired
    protected FacultyRepository facultyRepository;
    @Autowired
    protected AdministratorRepository administratorRepository;
    @Autowired
    protected MaintainerRepository maintainerRepository;

    protected void baseTestForUser(User user) {
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(LocalDate.of(1995, 11, 6), user.getBirthDate());
        assertEquals(Gender.MALE, user.getGender());
    }

    protected User createUserCommonFields(User user) {
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBirthDate(LocalDate.of(1995, 11, 6));
        user.setGender(Gender.MALE);
        return user;
    }

    protected UserDTO createUserDTOCommonFields(UserDTO userDTO) {
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setBirthDate(LocalDate.of(1995, 11, 6));
        userDTO.setGender(Gender.MALE);
        return userDTO;
    }

    protected MaintainerDTO createMaintainerDTO() {
        MaintainerDTO maintainerDTO = new MaintainerDTO();
        maintainerDTO.setId(DEFAULT_ID);
        createUserDTOCommonFields(maintainerDTO);
        return maintainerDTO;
    }

    protected Maintainer createMaintainer() {
        Maintainer maintainer = new Maintainer();
        maintainer.setId(DEFAULT_ID);
        createUserCommonFields(maintainer);
        return maintainer;
    }

    protected Administrator createAdministrator() {
        Administrator administrator = new Administrator();
        administrator.setId(DEFAULT_ID);
        createUserCommonFields(administrator);
        return administrator;
    }

    protected AdministratorDTO createAdministratorDTO() {
        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setId(DEFAULT_ID);
        createUserDTOCommonFields(administratorDTO);
        return administratorDTO;
    }

    protected Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        createUserCommonFields(student);
        student.setGroup(savedGroup());
        return student;
    }

    protected StudentDTO createStudentDTO() {
        StudentDTO studentDTO = new StudentDTO();
        createUserDTOCommonFields(studentDTO);
        studentDTO.setId(DEFAULT_ID);
        studentDTO.setGroup(savedGroup());
        return studentDTO;
    }

    protected Teacher createTeacher() {
        Teacher teacher = new Teacher();
        createUserCommonFields(teacher);
        teacher.setId(DEFAULT_ID);
        teacher.setAcademicDegree("Ph.D");
        return teacher;
    }

    protected TeacherDTO createTeacherDTO() {
        TeacherDTO teacherDTO = new TeacherDTO();
        createUserDTOCommonFields(teacherDTO);
        teacherDTO.setId(DEFAULT_ID);
        teacherDTO.setAcademicDegree("Ph.D");
        return teacherDTO;
    }

    private Teacher savedTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(DEFAULT_ID);
        createUserCommonFields(teacher);
        teacher.setAcademicDegree("Ph.D");
        return teacherRepository.save(teacher);
    }

    protected Course createCourse() {
        Course course = new Course();
        course.setFaculty(createFaculty());
        course.setId(1L);
        course.setName("Course 101");
        return course;
    }

    protected Faculty createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(DEFAULT_ID);
        faculty.setName("Faculty of Science");
        return faculty;
    }

    protected Lecture createLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(DEFAULT_ID);
        lecture.setName("Introduction to Programming");
        lecture.setDescription(
                "Test.Introduction to Programming.Test.Test.Introduction to Programming.Test.");
        lecture.setCourse(savedCourse());
        lecture.setTeacher(savedTeacher());
        lecture.setDate(LocalDateTime.of(2025, 4, 23, 8, 30, 0));
        lecture.setGroups(new ArrayList<>());
        return lecture;
    }

    protected Group createGroup() {
        Group group = new Group();
        group.setId(DEFAULT_ID);
        group.setName("AB-12");
        return group;
    }

    private Group savedGroup() {
        Group group = new Group();
        group.setId(DEFAULT_ID);
        group.setName("AB-12");
        return groupRepository.save(group);
    }

    private Course savedCourse() {
        Course course = new Course();
        course.setId(DEFAULT_ID);
        course.setName("Testcourse");
        course.setFaculty(savedFaculty());
        return courseRepository.save(course);
    }

    private Faculty savedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(DEFAULT_ID);
        faculty.setName("Testfaculty");
        return facultyRepository.save(faculty);
    }
}
