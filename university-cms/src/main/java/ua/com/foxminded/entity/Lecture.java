package ua.com.foxminded.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "lectures")
public class Lecture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lecture_id")
  private Long id;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "course_id")
  @NotNull
  private Course course;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "teacher_id")
  @NotNull
  private Teacher teacher;

  @ManyToMany(mappedBy = "lectures")
  private List<Group> groups;

  @Column(name = "lecture_name")
  @NotNull
  private String name;

  @Column(name = "description")
  @NotNull
  @Pattern(regexp = "^.{50,2000}$")
  private String description;

  @Column(name = "lecture_date")
  @NotNull
  private LocalDateTime date;

  public Lecture(
          Long id,
          Course course,
          Teacher teacher,
          String name,
          String description,
          LocalDateTime date) {
    this.id = id;
    this.course = course;
    this.teacher = teacher;
    this.name = name;
    this.description = description;
    this.date = date;
    this.groups = new ArrayList<>();
  }

  public Lecture() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lecture lecture = (Lecture) o;
    return id.equals(lecture.id)
            && name.equals(lecture.name)
            && description.equals(lecture.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }

  @Override
  public String toString() {
    return "Lecture{"
            + "id="
            + id
            + ", name='"
            + name
            + '\''
            + ", description='"
            + description
            + '\''
            + ", date="
            + date
            + '}';
  }
}
