package com.app.domain.auth.repository;

import com.app.domain.auth.entity.UserAuthentication;
import java.util.Optional;

public interface UserAuthenticationRepository {
  Optional<UserAuthentication> findByUsername(String username);
  void save(UserAuthentication user);
}
