package ua.com.foxminded.service.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.repository.MaintainerRepository;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.UserEmailService;

@Service
public class UserEmailServiceImpl implements UserEmailService {
  private final AdministratorRepository administratorRepository;
  private final MaintainerRepository maintainerRepository;
  private final StudentRepository studentRepository;
  private final TeacherRepository teacherRepository;

  public UserEmailServiceImpl(
          AdministratorRepository administratorRepository,
          MaintainerRepository maintainerRepository,
          StudentRepository studentRepository,
          TeacherRepository teacherRepository) {
    this.administratorRepository = administratorRepository;
    this.maintainerRepository = maintainerRepository;
    this.studentRepository = studentRepository;
    this.teacherRepository = teacherRepository;
  }

  @Override
  public boolean isUserExistByEmail(String email) {
    return administratorRepository.findByEmail(email).isEmpty()
            && maintainerRepository.findByEmail(email).isEmpty()
            && studentRepository.findByEmail(email).isEmpty()
            && teacherRepository.findByEmail(email).isEmpty();
  }
}
