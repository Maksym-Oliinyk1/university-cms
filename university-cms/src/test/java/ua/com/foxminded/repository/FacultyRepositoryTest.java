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
import ua.com.foxminded.entity.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        FacultyRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class FacultyRepositoryTest extends BaseTest {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_FACULTY = """
            SELECT f
            FROM Faculty f
            WHERE f.name = :facultyName
            """;

    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(f) FROM Faculty f
            """;

    @Test
    void create() {
        Faculty expectedFaculty = new Faculty();
        expectedFaculty.setName("FacultyTest");
        facultyRepository.save(expectedFaculty);

        Faculty faculty = entityManager.find(Faculty.class, 1L);

        assertNotNull(faculty);
        assertEquals(expectedFaculty.getName(), faculty.getName());
    }

    @Test
    void findById() {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(2L);
        assertTrue(optionalFaculty.isPresent());
        Faculty faculty = optionalFaculty.get();
        assertEquals("Faculty2", faculty.getName());
    }

    @Test
    void update() {
        Faculty faculty = entityManager.find(Faculty.class, 2L);
        faculty.setName("UpdatedFaculty");

        facultyRepository.save(faculty);

        Faculty updatedFaculty = entityManager.createQuery(FIND_FACULTY, Faculty.class)
                .setParameter("facultyName", "UpdatedFaculty")
                .getSingleResult();

        assertNotNull(updatedFaculty);
        assertEquals("UpdatedFaculty", updatedFaculty.getName());
    }

    @Test
    void delete() {
        facultyRepository.deleteById(2L);
        Long facultyCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1, facultyCount);
    }

    @Test
    void findAll() {
        List<Faculty> expectedFaculties = new ArrayList<>();
        expectedFaculties.add(new Faculty(2L, "Faculty2"));
        expectedFaculties.add(new Faculty(3L, "Faculty3"));

        List<Faculty> actualFaculties = (List<Faculty>) facultyRepository.findAll();

        assertEquals(expectedFaculties, actualFaculties);
    }
}