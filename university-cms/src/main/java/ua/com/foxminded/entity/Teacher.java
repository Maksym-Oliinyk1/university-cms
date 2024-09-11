package ua.com.foxminded.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher extends User implements UserDetails {

  @Column(name = "academic_degree")
  private String academicDegree;
  @OneToMany(mappedBy = "teacher")
  private List<Lecture> lectures;

  public Teacher(
          Long id,
          String firstName,
          String lastName,
          Gender gender,
          String academicDegree,
          LocalDate birthDate,
          String email,
          String imageName,
          String password,
          Authorities authority) {
    super(firstName, lastName, gender, birthDate, email, imageName, password, authority);
    this.id = id;
    this.academicDegree = academicDegree;
    this.lectures = new ArrayList<>();
  }

  public Teacher() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(authority.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAcademicDegree() {
    return academicDegree;
  }

  public void setAcademicDegree(String academicDegree) {
    this.academicDegree = academicDegree;
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
    Teacher teacher = (Teacher) o;
    return id.equals(teacher.id)
            && firstName.equals(teacher.firstName)
            && lastName.equals(teacher.lastName)
            && academicDegree.equals(teacher.academicDegree);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, academicDegree);
  }

  @Override
  public String toString() {
    return "Teacher{"
            + "id="
            + id
            + ", firstName='"
            + firstName
            + '\''
            + ", lastName='"
            + lastName
            + '\''
            + ", academicDegree='"
            + academicDegree
            + '\''
            + '}';
  }
}
