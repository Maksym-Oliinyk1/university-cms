package ua.com.foxminded.service;

import org.springframework.stereotype.Service;

@Service
public interface UserEmailService {

  boolean isUserExistByEmail(String email);
}
