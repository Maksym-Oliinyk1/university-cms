package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Faculty;

@Repository
public interface FacultyRepository extends CrudRepository<Faculty, Long> {

}
