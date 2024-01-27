package ua.com.foxminded.service;

import org.springframework.web.multipart.MultipartFile;


public interface UserService<E> extends BaseService<E> {
    void save(E user, MultipartFile imageFile);

    void update(Long id, E user, MultipartFile imageFile);
}
