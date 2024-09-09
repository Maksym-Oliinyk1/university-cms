package ua.com.foxminded.service.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.User;
import ua.com.foxminded.repository.UserEmailRepository;
import ua.com.foxminded.service.UserEmailService;

import java.util.Optional;

@Service
public class UserEmailServiceImpl implements UserEmailService {
  private final UserEmailRepository userEmailRepository;

  public UserEmailServiceImpl(UserEmailRepository userEmailRepository) {
    this.userEmailRepository = userEmailRepository;
  }

  @Override
  public User getUserByEmail(String email) {
    Optional<User> user = userEmailRepository.findByEmail(email);
    if (user.isPresent()) {
      return user.get();
    }
    throw new RuntimeException("There is no user with this email");
  }

  @Override
  public boolean isUserExistByEmail(String email) {
    return userEmailRepository.findByEmail(email).isPresent();
  }
}
