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
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        CourseRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class CourseRepositoryTest extends BaseTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_COURSE = """
            SELECT c
            FROM Course c
            WHERE c.name = :courseName
            """;

    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(c) FROM Course c
            """;

    @Test
    void create() {
        Faculty faculty = entityManager.find(Faculty.class, 2L);
        Course expectedCourse = new Course(0L, "CourseTest", faculty);
        courseRepository.save(expectedCourse);

        Course course = entityManager.find(Course.class, 1L);
        assertNotNull(course);
        assertEquals(expectedCourse.getName(), course.getName());
        assertEquals(expectedCourse.getFaculty(), course.getFaculty());
    }

    @Test
    void findById() {
        Optional<Course> optionalCourse = courseRepository.findById(2L);
        assertTrue(optionalCourse.isPresent());
        Course course = optionalCourse.get();
        assertEquals("Course2", course.getName());
        assertNotNull(course.getFaculty());
    }

    @Test
    void update() {
        Course course = entityManager.find(Course.class, 2L);
        course.setName("UpdatedCourse");

        courseRepository.save(course);

        Course updatedCourse = entityManager.createQuery(FIND_COURSE, Course.class)
                .setParameter("courseName", "UpdatedCourse")
                .getSingleResult();

        assertNotNull(updatedCourse);
        assertEquals("UpdatedCourse", updatedCourse.getName());
    }

    @Test
    void delete() {
        courseRepository.deleteById(3L);
        Long courseCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1, courseCount);
    }

    @Test
    void findAll() {
        List<Course> expectedCourses = new ArrayList<>();
        Faculty facultyOne = entityManager.find(Faculty.class, 2L);
        Faculty facultyTwo = entityManager.find(Faculty.class, 3L);

        expectedCourses.add(new Course(2L, "Course2", facultyOne));
        expectedCourses.add(new Course(3L, "Course3", facultyTwo));

        List<Course> actualCourses = (List<Course>) courseRepository.findAll();

        assertEquals(expectedCourses, actualCourses);
    }
}