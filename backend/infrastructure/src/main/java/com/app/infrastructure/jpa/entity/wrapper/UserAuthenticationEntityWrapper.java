package com.app.infrastructure.jpa.entity.wrapper;

import com.app.infrastructure.jpa.entity.UserAuthenticationEntity;
import java.util.Objects;

public class UserAuthenticationEntityWrapper {
  private UserAuthenticationEntity entity;

  private final FieldWrapper<Long> id;
  private final FieldWrapper<String> username;
  private final FieldWrapper<String> password;

  public UserAuthenticationEntityWrapper(
      UserAuthenticationEntity userAuthenticationEntity) {
    this.entity = userAuthenticationEntity;

    this.id = new FieldWrapper<>(userAuthenticationEntity, "id");
    this.username = new FieldWrapper<>(userAuthenticationEntity, "username");
    this.password = new FieldWrapper<>(userAuthenticationEntity, "password");
  }

  public Long id() {
    return this.id.get();
  }

  public void setId(Long value) {
    this.id.set(value);
  }

  public String username() {
    return this.username.get();
  }

  public void setUsername(String value) {
    this.username.set(value);
  }

  public String password() {
    return this.password.get();
  }

  public void setPassword(String value) {
    this.password.set(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAuthenticationEntityWrapper that = (UserAuthenticationEntityWrapper) o;
    return Objects.equals(id(), that.id()) && Objects.equals(username(), that.username())
        && Objects.equals(password(), that.password());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id(), username(), password());
  }

  @Override
  public String toString() {
    return "UserAuthenticationEntityWrapper{"
        + "id=" + id()
        + ", username='" + username() + '\''
        + ", password='" + password() + '\''
        + '}';
  }

  public UserAuthenticationEntity entity() {
    return entity;
  }
}
