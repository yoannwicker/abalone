package com.app.domain.auth.usecase;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.repository.UserAuthenticationRepository;
import java.util.Optional;

public record FindUserAuthentication(UserAuthenticationRepository userAuthenticationRepository) {

  public Optional<UserAuthentication> byUsername(String username) {
    return userAuthenticationRepository.findByUsername(username);
  }
}
