package com.app.domain.auth.usecase;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.repository.UserAuthenticationRepository;

public record SaveUserAuthentication(UserAuthenticationRepository userAuthenticationRepository) {

  public void save(UserAuthentication userAuthentication) {
    userAuthenticationRepository.findByUsername(userAuthentication.username())
        .ifPresentOrElse(
            user -> {
              throw new IllegalArgumentException("User already exists");
            },
            () -> userAuthenticationRepository.save(userAuthentication));
  }
}
