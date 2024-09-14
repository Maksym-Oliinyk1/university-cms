package ua.com.foxminded.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.MaintainerService;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.TeacherService;

@Service
public class AuthenticationService {
  private final AdministratorService administratorService;
  private final MaintainerService maintainerService;
  private final StudentService studentService;
  private final TeacherService teacherService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(
          AdministratorService administratorService,
          MaintainerService maintainerService,
          StudentService studentService,
          TeacherService teacherService,
          PasswordEncoder passwordEncoder,
          JwtService jwtService,
          AuthenticationManager authenticationManager) {
    this.administratorService = administratorService;
    this.maintainerService = maintainerService;
    this.studentService = studentService;
    this.teacherService = teacherService;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthenticationResponse registerAdmin(AdministratorDTO administratorDTO) {
    administratorDTO.setPassword(passwordEncoder.encode(administratorDTO.getPassword()));
    Administrator administrator = administratorService.save(administratorDTO);
    String jwtToken = jwtService.generateToken(administrator);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse registerMaintainer(MaintainerDTO maintainerDTO) {
    maintainerDTO.setPassword(passwordEncoder.encode(maintainerDTO.getPassword()));
    Maintainer maintainer = maintainerService.save(maintainerDTO);
    String jwtToken = jwtService.generateToken(maintainer);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse registerStudent(StudentDTO studentDTO) {
    studentDTO.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
    Student student = studentService.save(studentDTO);
    String jwtToken = jwtService.generateToken(student);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse registerTeacher(TeacherDTO teacherDTO) {
    teacherDTO.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
    Teacher teacher = teacherService.save(teacherDTO);
    String jwtToken = jwtService.generateToken(teacher);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse updateAdmin(Long id, AdministratorDTO administratorDTO) {
    administratorDTO.setPassword(passwordEncoder.encode(administratorDTO.getPassword()));
    Administrator administrator = administratorService.update(id, administratorDTO);
    String jwtToken = jwtService.generateToken(administrator);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse updateMaintainer(Long id, MaintainerDTO maintainerDTO) {
    maintainerDTO.setPassword(passwordEncoder.encode(maintainerDTO.getPassword()));
    Maintainer maintainer = maintainerService.update(id, maintainerDTO);
    String jwtToken = jwtService.generateToken(maintainer);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse updateStudent(Long id, StudentDTO studentDTO) {
    studentDTO.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
    Student student = studentService.update(id, studentDTO);
    String jwtToken = jwtService.generateToken(student);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse updateTeacher(Long id, TeacherDTO teacherDTO) {
    teacherDTO.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
    Teacher teacher = teacherService.update(id, teacherDTO);
    String jwtToken = jwtService.generateToken(teacher);
    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse signIn(AuthenticationRequest request) {
    try {
      Authentication authentication =
              authenticationManager.authenticate(
                      new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String jwt = jwtService.generateToken(userDetails);
      return new AuthenticationResponse(jwt);
    } catch (BadCredentialsException e) {
      throw new UsernameNotFoundException("Invalid email or password");
    }
  }
}
