package ua.com.foxminded.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GroupRepository
        extends PagingAndSortingRepository<Group, Long>, CrudRepository<Group, Long> {
    @Query(
            "SELECT l FROM Lecture l JOIN l.groups g WHERE g.id = :groupId AND l.date BETWEEN :startDateTime AND :endDateTime")
    List<Lecture> findLecturesByDateBetween(
            @Param("groupId") Long groupId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT COUNT(l) FROM Lecture l JOIN l.groups g WHERE g.id = :groupId")
    Long countLecturesByGroup(@Param("groupId") Long groupId);

    Page<Group> findAllByLectures_Id(Long lectureId, Pageable pageable);
}
