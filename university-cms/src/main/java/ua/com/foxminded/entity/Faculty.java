package ua.com.foxminded.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "faculties")
public class Faculty {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "faculty_id")
  private Long id;

  @Column(name = "faculty_name")
  private String name;

  @OneToMany(mappedBy = "faculty")
  @JsonManagedReference
  private List<Course> courses;

  public Faculty(Long id, String name) {
    this.id = id;
    this.name = name;
    this.courses = new ArrayList<>();
  }

    public Faculty() {
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

  public List<Course> getCourses() {
    return courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Faculty faculty = (Faculty) o;
    return id.equals(faculty.id) && name.equals(faculty.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "Faculty{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
