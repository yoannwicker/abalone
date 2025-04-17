package com.app.domain.auth.usecase;

import com.app.domain.auth.entity.AuthenticationInformation;
import com.app.domain.common.repository.CurrentDateProvider;
import com.app.domain.auth.repository.TokenRepository;
import java.util.Date;

public record ManageToken(TokenRepository tokenRepository, CurrentDateProvider currentDateProvider) {

  public boolean validate(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  public String extractUsername(String token) {
    return tokenRepository.extractAuthenticationInformations(token, AuthenticationInformation::username);
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(currentDateProvider.getCurrentDate());
  }

  private Date extractExpiration(String token) {
    return tokenRepository.extractAuthenticationInformations(token, AuthenticationInformation::expiration);
  }
}
