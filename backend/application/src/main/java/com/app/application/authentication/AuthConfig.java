package com.app.application.authentication;

import com.app.domain.auth.repository.UserAuthenticationRepository;
import com.app.domain.auth.usecase.FindUserAuthentication;
import com.app.domain.auth.usecase.SaveUserAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

  @Bean
  public FindUserAuthentication findUserAuthentication(
      UserAuthenticationRepository userAuthenticationRepository) {
    return new FindUserAuthentication(userAuthenticationRepository);
  }

  @Bean
  public SaveUserAuthentication saveUserAuthentication(
      UserAuthenticationRepository userAuthenticationRepository) {
    return new SaveUserAuthentication(userAuthenticationRepository);
  }
}
