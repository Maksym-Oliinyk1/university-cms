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
import ua.com.foxminded.entity.Lecture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        GroupRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
class GroupRepositoryTest extends BaseTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_GROUP = """
            SELECT g
            FROM Group g
            WHERE g.name = :groupName
            """;

    private static final String FIND_COUNT_ALL = """
            SELECT COUNT(g) FROM Group g
            """;

    @Test
    void create() {
        Group expectedGroup = new Group(0L, "GroupTest");
        groupRepository.save(expectedGroup);

        Group group = entityManager.find(Group.class, 1L);

        assertNotNull(group);
        assertEquals(expectedGroup.getName(), group.getName());
    }

    @Test
    void findById() {
        Optional<Group> optionalGroup = groupRepository.findById(2L);
        assertTrue(optionalGroup.isPresent());
        Group group = optionalGroup.get();
        assertEquals("Group2", group.getName());
    }

    @Test
    void update() {
        Group group = entityManager.find(Group.class, 2L);

        groupRepository.save(group);

        Group updatedGroup = entityManager.createQuery(FIND_GROUP, Group.class)
                .setParameter("groupName", "UpdatedGroup")
                .getSingleResult();

        assertNotNull(updatedGroup);
        assertEquals("UpdatedGroup", updatedGroup.getName());
    }

    @Test
    void delete() {
        groupRepository.deleteById(2L);
        Long groupCount = entityManager.createQuery(FIND_COUNT_ALL, Long.class)
                .getSingleResult();
        assertEquals(1, groupCount);
    }

    @Test
    void findAll() {
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(new Group(2L, "Group2"));
        expectedGroups.add(new Group(3L, "Group3"));

        List<Group> actualGroups = (List<Group>) groupRepository.findAll();

        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void findLecturesByDateBetween() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

        List<Lecture> lectures = groupRepository.findLecturesByDateBetween(3L, startDateTime, endDateTime);

        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals("Lecture3", lectures.get(0).getName());
    }

    @Test
    void countLecturesByGroup() {
        Long lectureCount = groupRepository.countLecturesByGroup(3L);

        assertNotNull(lectureCount);
        assertEquals(1L, lectureCount);
    }
}