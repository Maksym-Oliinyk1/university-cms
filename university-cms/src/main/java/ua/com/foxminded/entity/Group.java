package ua.com.foxminded.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "groups")
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long id;

  @Column(name = "group_name")
  @Pattern(regexp = "^[A-Z]{2}-\\d{2}$")
  @NotNull
  private String name;

  @OneToMany(mappedBy = "group")
  private List<Student> students;

  @ManyToMany
  @JoinTable(
          name = "group_lecture",
          joinColumns = @JoinColumn(name = "group_id"),
          inverseJoinColumns = @JoinColumn(name = "lecture_id"))
  private List<Lecture> lectures;

  public Group(Long id, String name) {
    this.id = id;
    this.name = name;
    this.students = new ArrayList<>();
    this.lectures = new ArrayList<>();
  }

  public Group() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }

  public List<Lecture> getLectures() {
    return lectures;
  }

  public void setLectures(List<Lecture> lectures) {
    this.lectures = lectures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Group group = (Group) o;
    return id.equals(group.id) && name.equals(group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "Group{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
