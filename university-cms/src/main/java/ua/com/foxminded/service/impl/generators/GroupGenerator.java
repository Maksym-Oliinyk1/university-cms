package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

import java.util.List;

@Service
public class GroupGenerator extends DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(GroupGenerator.class);
    private static final String GROUP_NAMES_DIRECTORY = "/populate/group_names";
    private static final List<String> GROUP_NAMES = readFilePerOneLine(GROUP_NAMES_DIRECTORY);
    private static final int AMOUNT_OF_GROUPS = 20;

    private final GroupService groupService;

    public GroupGenerator(GroupService groupService) {
        this.groupService = groupService;
    }

    public void generateIfEmpty() {
        if (groupService.count() == 0) {
            generateGroups();
        }
    }

    @Override
    public int getOrder() {
        return 6;
    }

    private void generateGroups() {
        for (int i = 0; i < AMOUNT_OF_GROUPS; i++) {
            String groupName = GROUP_NAMES.get(random.nextInt(GROUP_NAMES.size()));
            Group group = new Group();
            group.setName(groupName);
            logger.info("Created group: {}", group.getName());
            groupService.save(group);
        }
    }
}
