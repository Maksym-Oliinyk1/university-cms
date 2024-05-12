package ua.com.foxminded.service;

import java.util.Optional;

public interface UserService<E, D> extends BaseService<E> {
    E save(D user);

    E update(Long id, D user);

    D findByIdDTO(Long id);

    Optional<E> findByEmail(String email);
}
