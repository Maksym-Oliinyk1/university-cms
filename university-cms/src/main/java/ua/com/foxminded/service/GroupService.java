package ua.com.foxminded.service;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

import java.util.List;

public interface GroupService extends BaseService<Group> {
    void attachStudentToGroup(Long studentId, Long groupId);

    void detachStudentFromGroup(Long studentId, Long groupId);

    List<Lecture> showLecturesPerWeek(Long groupId);

    List<Lecture> showLecturesPerMonth(Long groupId);
}
