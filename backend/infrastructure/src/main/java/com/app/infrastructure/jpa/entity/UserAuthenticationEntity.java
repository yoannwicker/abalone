package com.app.infrastructure.jpa.entity;

import com.app.domain.auth.entity.UserAuthentication;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "app_user")
public class UserAuthenticationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;

  public Long getId() {
    return id;
  }

  public UserAuthentication toDomain() {
    return new UserAuthentication(username, password);
  }

  public static UserAuthenticationEntity fromDomain(UserAuthentication user) {
    UserAuthenticationEntity entity = new UserAuthenticationEntity();
    entity.username = user.username();
    entity.password = user.password();
    return entity;
  }
}
