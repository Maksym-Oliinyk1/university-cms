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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        StudentRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class StudentRepositoryTest extends BaseTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_STUDENT = """
            SELECT s
            FROM Student s
            WHERE s.firstName = :firstName AND s.lastName = :lastName
            """;

    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(s) FROM Student s
            """;

    @Test
    void create() {
        Student expectedStudent = new Student(0L, "StudentFirstName", "StudentLastName");
        Group group = entityManager.find(Group.class, 2L);
        expectedStudent.setGroup(group);
        studentRepository.save(expectedStudent);

        Student student = entityManager.find(Student.class, 1L);

        assertNotNull(student);
        assertEquals(expectedStudent.getFirstName(), student.getFirstName());
        assertEquals(expectedStudent.getLastName(), student.getLastName());
        assertEquals(expectedStudent.getGroup(), group);
    }

    @Test
    void findById() {
        Optional<Student> optionalStudent = studentRepository.findById(2L);
        assertTrue(optionalStudent.isPresent());
        Student student = optionalStudent.get();
        assertEquals("Student2", student.getFirstName());
        assertEquals("LastName2", student.getLastName());
    }

    @Test
    void update() {
        Student student = entityManager.find(Student.class, 2L);
        student.setFirstName("UpdatedFirstName");
        student.setLastName("UpdatedLastName");

        studentRepository.save(student);

        Student updatedStudent = entityManager.createQuery(FIND_STUDENT, Student.class)
                .setParameter("firstName", "UpdatedFirstName")
                .setParameter("lastName", "UpdatedLastName")
                .getSingleResult();

        assertNotNull(updatedStudent);
        assertEquals("UpdatedFirstName", updatedStudent.getFirstName());
        assertEquals("UpdatedLastName", updatedStudent.getLastName());
    }

    @Test
    void delete() {
        studentRepository.deleteById(2L);
        Long studentCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1L, studentCount);
    }

    @Test
    void findAll() {
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(new Student(2L, "Student2", "LastName2"));
        expectedStudents.add(new Student(3L, "Student3", "LastName3"));

        List<Student> actualStudents = (List<Student>) studentRepository.findAll();

        assertEquals(expectedStudents, actualStudents);
    }
}