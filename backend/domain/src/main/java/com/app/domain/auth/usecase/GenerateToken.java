package com.app.domain.auth.usecase;

import com.app.domain.auth.repository.TokenRepository;

public record GenerateToken(TokenRepository tokenRepository) {

  public String execute(String username) {
    return tokenRepository.generateToken(username);
  }
}
