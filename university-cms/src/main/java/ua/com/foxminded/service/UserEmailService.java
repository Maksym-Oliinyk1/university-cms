package ua.com.foxminded.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.User;

@Service
public interface UserEmailService {
    User getUserByEmail(String email);

    boolean isUserExistByEmail(String email);
}
