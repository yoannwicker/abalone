package com.app.application.authentication;

import com.app.domain.auth.repository.TokenParameterProvider;
import com.app.domain.auth.repository.TokenRepository;
import com.app.domain.auth.usecase.GenerateToken;
import com.app.domain.auth.usecase.ManageToken;
import com.app.domain.common.repository.CurrentDateProvider;
import com.app.infrastructure.jjwt.TokenJjwtRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long jwtExpirationInMillis;

  @Bean
  public TokenParameterProvider tokenParameterProvider() {
    return new TokenParameterProvider() {
      @Override
      public String secret() {
        return secret;
      }

      @Override
      public long jwtExpirationInMillis() {
        return jwtExpirationInMillis;
      }
    };
  }

  @Bean
  public TokenRepository tokenRepository(TokenParameterProvider tokenParameterProvider) {
    return new TokenJjwtRepository(tokenParameterProvider);
  }

  @Bean
  public GenerateToken generateToken(TokenRepository tokenRepository) {
    return new GenerateToken(tokenRepository);
  }

  @Bean
  public ManageToken manageToken(TokenRepository tokenRepository,
      CurrentDateProvider currentDateProvider) {
    return new ManageToken(tokenRepository, currentDateProvider);
  }
}
