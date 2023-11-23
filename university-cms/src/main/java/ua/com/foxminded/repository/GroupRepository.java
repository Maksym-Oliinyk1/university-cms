package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
}
