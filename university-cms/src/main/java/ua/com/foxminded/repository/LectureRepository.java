package ua.com.foxminded.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Lecture;

@Repository
public interface LectureRepository extends CrudRepository<Lecture, Long> {

}
