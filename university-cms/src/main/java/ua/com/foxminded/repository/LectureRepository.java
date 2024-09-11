package ua.com.foxminded.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Lecture;

@Repository
public interface LectureRepository
        extends PagingAndSortingRepository<Lecture, Long>, CrudRepository<Lecture, Long> {
  Page<Lecture> findByCourseId(Long courseId, Pageable pageable);

  Page<Lecture> findAllByGroups_Id(Long groupId, Pageable pageable);
}
