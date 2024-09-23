package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.service.LectureService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {

  private static final Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);

  private final LectureRepository lectureRepository;
  private final GroupRepository groupRepository;

  @Autowired
  public LectureServiceImpl(LectureRepository lectureRepository, GroupRepository groupRepository) {
    this.lectureRepository = lectureRepository;
    this.groupRepository = groupRepository;
  }

  @Override
  @Transactional
  public void save(Lecture lecture) {
    List<Group> lectureGroups = lecture.getGroups();
    Lecture savedLecture = lectureRepository.save(lecture);
    attachGroupsOfLecture(savedLecture.getId(), lectureGroups);
    lectureRepository.save(savedLecture);
    logger.info("Saved lecture: {}", lecture.getName());
  }

  @Transactional
  protected void attachGroupsOfLecture(Long lectureId, List<Group> lectureGroups) {
    List<Group> groupsCopy = new ArrayList<>(lectureGroups);
    for (Group lectureGroup : groupsCopy) {
      attachGroupToLecture(lectureGroup.getId(), lectureId);
    }
  }

  @Transactional
  protected void detachGroupsFromLecture(Long lectureId, List<Group> lectureGroups) {
    List<Group> groupsCopy = new ArrayList<>(lectureGroups);
    for (Group lectureGroup : groupsCopy) {
      detachGroupFromLecture(lectureGroup.getId(), lectureId);
    }
  }

  @Override
  @Transactional
  public void update(Long id, Lecture updatedLecture) {
    Optional<Lecture> optionalLecture = lectureRepository.findById(id);
    if (optionalLecture.isPresent()) {
      List<Group> newLectureGroups = updatedLecture.getGroups();
      Lecture existingLecture = optionalLecture.get();

      detachGroupsFromLecture(id, existingLecture.getGroups());

      existingLecture.setName(updatedLecture.getName());
      existingLecture.setDescription(updatedLecture.getDescription());
      existingLecture.setTeacher(updatedLecture.getTeacher());
      existingLecture.setCourse(updatedLecture.getCourse());
      existingLecture.setGroups(newLectureGroups);

      attachGroupsOfLecture(id, newLectureGroups);

      lectureRepository.save(existingLecture);
      logger.info("Lecture updated by id: {}", id);
    } else {
      throw new RuntimeException("There is no such lecture");
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (lectureRepository.existsById(id)) {
      List<Group> lectureGroups = lectureRepository.findById(id).get().getGroups();
      detachGroupsFromLecture(id, lectureGroups);
      lectureRepository.deleteById(id);
      logger.info("The lecture was deleted by id {}", id);
    } else {
      throw new RuntimeException("There is no such lecture");
    }
  }

  @Override
  @Transactional
  public Lecture findById(Long id) {
    Optional<Lecture> optionalLecture = lectureRepository.findById(id);
    if (optionalLecture.isPresent()) {
      logger.info("The lecture was found by id {}", id);
      return optionalLecture.get();
    } else {
      throw new RuntimeException("There is no such lecture");
    }
  }

  @Override
  public Page<Lecture> findAll(Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    int from = pageNumber * pageSize;
    int to = from + pageSize;
    logger.info("Find lectures from {} to {}", from, to);
    return lectureRepository.findAll(pageable);
  }

  @Override
  public Long count() {
    return lectureRepository.count();
  }

  @Override
  @Transactional
  public void attachGroupToLecture(Long groupId, Long lectureId) {
    Lecture lecture =
            lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);
    Group group = groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

    lecture.getGroups().add(group);
    group.getLectures().add(lecture);
    lectureRepository.save(lecture);
    groupRepository.save(group);
    logger.info("Group added to the lecture");
  }

  @Override
  @Transactional
  public void detachGroupFromLecture(Long groupId, Long lectureId) {
    Lecture lecture =
            lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);
    Group group = groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

    lecture.getGroups().remove(group);
    group.getLectures().remove(lecture);
    lectureRepository.save(lecture);
    groupRepository.save(group);
    logger.info("Group removed from the lecture");
  }

  @Override
  public Page<Lecture> findAllByCourse(Long courseId, Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    int from = pageNumber * pageSize;
    int to = from + pageSize;
    logger.info("Find lectures of course: {} from {} to {}", courseId, from, to);
    return lectureRepository.findByCourseId(courseId, pageable);
  }

  @Override
  public Page<Lecture> findAllByGroup(Long groupId, Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    int from = pageNumber * pageSize;
    int to = from + pageSize;
    logger.info("Find lectures of group: {} from {} to {}", groupId, from, to);
    return lectureRepository.findAllByGroups_Id(groupId, pageable);
  }
}
