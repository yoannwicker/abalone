package com.app.domain.auth.usecase;

import com.app.domain.auth.entity.AuthenticationInformation;
import com.app.domain.auth.repository.TokenRepository;
import java.util.Date;
import java.util.function.Supplier;

public record ManageToken(TokenRepository tokenRepository, Supplier<Date> currentDateProvider) {

  public boolean validate(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  public String extractUsername(String token) {
    return tokenRepository.extractAuthenticationInformations(
        token, AuthenticationInformation::username);
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(currentDateProvider.get());
  }

  private Date extractExpiration(String token) {
    return tokenRepository.extractAuthenticationInformations(
        token, AuthenticationInformation::expiration);
  }
}
