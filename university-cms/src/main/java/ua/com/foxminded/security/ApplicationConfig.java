package ua.com.foxminded.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.MaintainerService;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.TeacherService;

import java.util.Optional;

@Configuration
public class ApplicationConfig {
  private final AdministratorService administratorService;
  private final MaintainerService maintainerService;
  private final StudentService studentService;
  private final TeacherService teacherService;

  public ApplicationConfig(
          AdministratorService administratorService,
          MaintainerService maintainerService,
          StudentService studentService,
          TeacherService teacherService) {
    this.administratorService = administratorService;
    this.maintainerService = maintainerService;
    this.studentService = studentService;
    this.teacherService = teacherService;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return email -> {
      Optional<Administrator> adminOptional = administratorService.findByEmail(email);
      if (adminOptional.isPresent()) {
        return adminOptional.get();
      }

      Optional<Maintainer> maintainerOptional = maintainerService.findByEmail(email);
      if (maintainerOptional.isPresent()) {
        return maintainerOptional.get();
      }

      Optional<Student> studentOptional = studentService.findByEmail(email);
      if (studentOptional.isPresent()) {
        return studentOptional.get();
      }

      Optional<Teacher> teacherOptional = teacherService.findByEmail(email);
      if (teacherOptional.isPresent()) {
        return teacherOptional.get();
      }

      throw new UsernameNotFoundException("User not found");
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
          throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
