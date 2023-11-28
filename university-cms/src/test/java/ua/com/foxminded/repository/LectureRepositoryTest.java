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
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        LectureRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class LectureRepositoryTest extends BaseTest {

    private static final String FIND_LECTURE = """
            SELECT l
            FROM Lecture l
            WHERE l.name = :lectureName
            """;
    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(l) FROM Lecture l
            """;
    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void create() {
        Course course2 = entityManager.find(Course.class, 2L);
        Teacher teacher3 = entityManager.find(Teacher.class, 3L);

        Lecture expectedLecture = new Lecture(1L, course2, teacher3, "Lecture2", "Description2", LocalDateTime.now());
        lectureRepository.save(expectedLecture);

        Lecture lecture = entityManager.find(Lecture.class, 1L);

        assertNotNull(lecture);
        assertEquals(expectedLecture.getName(), lecture.getName());
        assertEquals(expectedLecture.getDescription(), lecture.getDescription());
        assertEquals(expectedLecture.getCourse(), lecture.getCourse());
        assertEquals(expectedLecture.getTeacher(), lecture.getTeacher());
    }

    @Test
    void findById() {
        Optional<Lecture> optionalLecture = lectureRepository.findById(2L);
        assertTrue(optionalLecture.isPresent());
        Lecture lecture = optionalLecture.get();
        assertEquals("Lecture2", lecture.getName());
        assertEquals("Description2", lecture.getDescription());
    }

    @Test
    void update() {
        Lecture lecture = entityManager.find(Lecture.class, 2L);
        lecture.setName("UpdatedLecture");
        lecture.setDescription("UpdatedDescription");

        lectureRepository.save(lecture);

        Lecture updatedLecture = entityManager.createQuery(FIND_LECTURE, Lecture.class)
                .setParameter("lectureName", "UpdatedLecture")
                .getSingleResult();

        assertNotNull(updatedLecture);
        assertEquals("UpdatedLecture", updatedLecture.getName());
        assertEquals("UpdatedDescription", updatedLecture.getDescription());
    }

    @Test
    void delete() {
        lectureRepository.deleteById(2L);
        Long lectureCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1, lectureCount);
    }

    @Test
    void findAll() {
        List<Lecture> expectedLectures = new ArrayList<>();

        Course course2 = entityManager.find(Course.class, 2L);
        Teacher teacher3 = entityManager.find(Teacher.class, 3L);

        expectedLectures.add(new Lecture(2L, course2, teacher3, "Lecture2", "Description2", LocalDateTime.now()));

        Course course3 = entityManager.find(Course.class, 3L);
        expectedLectures.add(new Lecture(3L, course3, teacher3, "Lecture3", "Description3", LocalDateTime.now()));


        List<Lecture> actualLectures = (List<Lecture>) lectureRepository.findAll();

        assertEquals(expectedLectures, actualLectures);
    }
}