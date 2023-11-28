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
import ua.com.foxminded.entity.Administrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        AdministratorRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class AdministratorRepositoryTest extends BaseTest{
    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_ADMINISTRATOR = """
            SELECT a
            FROM Administrator a
            WHERE a.firstName = :firstName AND a.lastName = :lastName
            """;

    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(a) FROM Administrator a
            """;

    @Test
    void create() {
        Administrator expectedAdministrator = new Administrator(0L, "AdministratorFirstName", "AdministratorLastName");
        administratorRepository.save(expectedAdministrator);

        Administrator administrator = entityManager.find(Administrator.class, 1L);

        assertNotNull(administrator);
        assertEquals(expectedAdministrator.getFirstName(), administrator.getFirstName());
        assertEquals(expectedAdministrator.getLastName(), administrator.getLastName());
    }

    @Test
    void findById() {
        Optional<Administrator> optionalAdministrator = administratorRepository.findById(2L);
        assertTrue(optionalAdministrator.isPresent());
        Administrator administrator = optionalAdministrator.get();
        assertEquals("Admin2", administrator.getFirstName());
        assertEquals("LastName2", administrator.getLastName());
    }

    @Test
    void update() {
        Administrator administrator = entityManager.find(Administrator.class, 2L);
        administrator.setFirstName("UpdatedFirstName");
        administrator.setLastName("UpdatedLastName");

        administratorRepository.save(administrator);

        Administrator updatedAdministrator = entityManager.createQuery(FIND_ADMINISTRATOR, Administrator.class)
                .setParameter("firstName", "UpdatedFirstName")
                .setParameter("lastName", "UpdatedLastName")
                .getSingleResult();

        assertNotNull(updatedAdministrator);
        assertEquals("UpdatedFirstName", updatedAdministrator.getFirstName());
        assertEquals("UpdatedLastName", updatedAdministrator.getLastName());
    }

    @Test
    void delete() {
        administratorRepository.deleteById(2L);
        Long administratorCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1, administratorCount);
    }

    @Test
    void findAll() {
        List<Administrator> expectedAdministrators = new ArrayList<>();
        expectedAdministrators.add(new Administrator(2L, "Admin2", "LastName2"));
        expectedAdministrators.add(new Administrator(3L, "Admin3", "LastName3"));

        List<Administrator> actualAdministrators = (List<Administrator>) administratorRepository.findAll();

        assertEquals(expectedAdministrators, actualAdministrators);
    }
}
