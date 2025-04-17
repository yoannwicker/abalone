package com.app.application.authentication.dto;

import com.app.domain.auth.entity.UserAuthentication;

public record RegisterRequest(String username, String password) {
  public UserAuthentication toDomain(String encode) {
    return new UserAuthentication(username, encode);
  }
}
