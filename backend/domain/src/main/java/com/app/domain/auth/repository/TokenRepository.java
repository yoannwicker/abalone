package com.app.domain.auth.repository;

import com.app.domain.auth.entity.AuthenticationInformation;
import java.util.function.Function;

public interface TokenRepository {

  <T> T extractAuthenticationInformations(String token, Function<AuthenticationInformation, T> claimsResolver);
  String generateToken(String username);
}
