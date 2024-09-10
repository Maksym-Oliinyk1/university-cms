package ua.com.foxminded.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ua.com.foxminded.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass
public abstract class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "user_id")
  protected UUID id;

  @Column(name = "first_name")
  @Pattern(regexp = "[a-zA-Z]+", message = "First name should contain only letters")
  @NotNull
  protected String firstName;

  @Column(name = "last_name")
  @Pattern(regexp = "[a-zA-Z]+", message = "Last name should contain only letters")
  @NotNull
  protected String lastName;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  @NotNull
  protected Gender gender;

  @Column(name = "birth_date")
  @NotNull
  protected LocalDate birthDate;

  @Column(name = "email", unique = true)
  @Pattern(regexp = "^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")
  @NotNull
  protected String email;

  @Column(name = "password")
  @NotNull
  protected String password;

  @Column(name = "image_name")
  protected String imageName;

  protected User(
          String firstName,
          String lastName,
          Gender gender,
          LocalDate birthDate,
          String email,
          String imageName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.birthDate = birthDate;
    this.email = email;
    this.imageName = imageName;
  }

  protected User() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }
}
