package ua.com.foxminded.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupService extends EntityService<Group> {
    void attachStudentToGroup(Long studentId, Long groupId);

    void detachStudentFromGroup(Long studentId, Long groupId);

    List<Lecture> showLecturesBetweenDates(Long groupId, LocalDateTime firstDate, LocalDateTime secondDate);

    Page<Group> findAllByLecture(Long lectureId, Pageable pageable);
}
