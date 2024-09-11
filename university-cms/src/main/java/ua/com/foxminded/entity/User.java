package ua.com.foxminded.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.enums.Gender;

import java.time.LocalDate;

@MappedSuperclass
public abstract class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  protected Long id;

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

  @Column(name = "authority")
  @Enumerated(EnumType.STRING)
  @NotNull
  protected Authorities authority;

  protected User(
          String firstName,
          String lastName,
          Gender gender,
          LocalDate birthDate,
          String email,
          String imageName,
          String password,
          Authorities authority) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.birthDate = birthDate;
    this.email = email;
    this.imageName = imageName;
    this.password = password;
    this.authority = authority;
  }

  protected User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public Authorities getAuthority() {
    return authority;
  }

  public void setAuthority(Authorities authority) {
    this.authority = authority;
  }
}
