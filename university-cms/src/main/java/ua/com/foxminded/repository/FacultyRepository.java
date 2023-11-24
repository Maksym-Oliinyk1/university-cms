package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.foxminded.entity.Faculty;

public interface FacultyRepository extends CrudRepository<Faculty, Long> {

}
