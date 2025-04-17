package com.app.domain.auth.repository;

public interface TokenParameterProvider {

  String secret();

  long jwtExpirationInMillis();
}
