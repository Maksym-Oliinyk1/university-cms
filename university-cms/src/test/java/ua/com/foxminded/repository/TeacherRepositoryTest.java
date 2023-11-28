package ua.com.foxminded.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        TeacherRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class TeacherRepositoryTest extends BaseTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_TEACHER = """
            SELECT t
            FROM Teacher t
            WHERE t.firstName = :firstName AND t.lastName = :lastName
            """;

    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(t) FROM Teacher t
            """;

    @Test
    void create() {
        Teacher expectedTeacher = new Teacher(0L, "TeacherFirstName", "TeacherLastName", "PhD");
        teacherRepository.save(expectedTeacher);

        Teacher teacher = entityManager.find(Teacher.class, 1L);

        assertNotNull(teacher);
        assertEquals(expectedTeacher.getFirstName(), teacher.getFirstName());
        assertEquals(expectedTeacher.getLastName(), teacher.getLastName());
        assertEquals(expectedTeacher.getAcademicDegree(), teacher.getAcademicDegree());
    }

    @Test
    void findById() {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(2L);
        assertTrue(optionalTeacher.isPresent());
        Teacher teacher = optionalTeacher.get();
        assertEquals("Teacher2", teacher.getFirstName());
        assertEquals("LastName2", teacher.getLastName());
        assertEquals("PhD", teacher.getAcademicDegree());
    }

    @Test
    void update() {
        Teacher teacher = entityManager.find(Teacher.class, 2L);
        teacher.setFirstName("UpdatedFirstName");
        teacher.setLastName("UpdatedLastName");
        teacher.setAcademicDegree("UpdatedPhD");

        teacherRepository.save(teacher);

        Teacher updatedTeacher = entityManager.createQuery(FIND_TEACHER, Teacher.class)
                .setParameter("firstName", "UpdatedFirstName")
                .setParameter("lastName", "UpdatedLastName")
                .getSingleResult();

        assertNotNull(updatedTeacher);
        assertEquals("UpdatedFirstName", updatedTeacher.getFirstName());
        assertEquals("UpdatedLastName", updatedTeacher.getLastName());
        assertEquals("UpdatedPhD", updatedTeacher.getAcademicDegree());
    }

    @Test
    void delete() {
        teacherRepository.deleteById(2L);
        Long teacherCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1, teacherCount);
    }

    @Test
    void findAll() {
        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(new Teacher(2L, "Teacher2", "LastName2", "PhD"));
        expectedTeachers.add(new Teacher(3L, "Teacher3", "LastName3", "MSc"));

        List<Teacher> actualTeachers = (List<Teacher>) teacherRepository.findAll();

        assertEquals(expectedTeachers, actualTeachers);
    }

    @Test
    void findLecturesByDateBetween() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

        List<Lecture> lectures = teacherRepository.findLecturesByDateBetween(3L, startDateTime, endDateTime);

        assertNotNull(lectures);
        assertEquals(2, lectures.size());
        assertEquals("Lecture2", lectures.get(0).getName());
        assertEquals("Lecture3", lectures.get(1).getName());
    }

    @Test
    void countLecturesByTeacher() {
        Long lectureCount = teacherRepository.countLecturesByTeacher(3L);

        assertNotNull(lectureCount);
        assertEquals(2L, lectureCount);
    }
}