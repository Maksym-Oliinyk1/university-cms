package ua.com.foxminded.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.foxminded.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        TeacherRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class TeacherRepositoryTest extends BaseTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void findLecturesByDateBetween() {
        LocalDateTime startDateTime = LocalDateTime.of(2023, 12, 1, 12, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 12, 4, 15, 0);

        List<Lecture> actualLectures = teacherRepository.findLecturesByDateBetween(3L, startDateTime, endDateTime);

        assertNotNull(actualLectures);
        assertEquals(2, actualLectures.size());
        assertEquals("Lecture2", actualLectures.get(0).getName());
        assertEquals("Lecture3", actualLectures.get(1).getName());
    }

    @Test
    void countLecturesByTeacher() {
        Long actualLectureCount = teacherRepository.countLecturesByTeacher(3L);

        assertNotNull(actualLectureCount);
        assertEquals(2L, actualLectureCount);
    }
}