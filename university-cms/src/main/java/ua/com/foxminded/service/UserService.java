package ua.com.foxminded.service;

import java.util.UUID;

public interface UserService<D> extends BaseService<D> {
  D save(D user);

  D update(UUID id, D user);

  D findByIdDTO(UUID id);

  D findByEmail(String email);
}
