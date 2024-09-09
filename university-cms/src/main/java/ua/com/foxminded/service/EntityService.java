package ua.com.foxminded.service;

import java.util.UUID;

public interface EntityService<D> extends BaseService<D> {
  void save(D e);

  void update(UUID id, D e);
}
