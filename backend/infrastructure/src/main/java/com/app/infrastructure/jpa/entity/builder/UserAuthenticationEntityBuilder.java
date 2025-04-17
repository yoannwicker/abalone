package com.app.infrastructure.jpa.entity.builder;

import com.app.infrastructure.jpa.entity.UserAuthenticationEntity;
import com.app.infrastructure.jpa.entity.wrapper.UserAuthenticationEntityWrapper;

public class UserAuthenticationEntityBuilder extends EntityBuilder<UserAuthenticationEntity> {

  private UserAuthenticationEntityWrapper entityWrapper;

  public static UserAuthenticationEntityBuilder userAuthenticationEntityBuilder() {
    return new UserAuthenticationEntityBuilder(new UserAuthenticationEntity());
  }

  public UserAuthenticationEntityBuilder(UserAuthenticationEntity entity) {
    this.entityWrapper = new UserAuthenticationEntityWrapper(entity);
  }

  @Override
  UserAuthenticationEntity entity() {
    return entityWrapper.entity();
  }

  public static UserAuthenticationEntityBuilder defaultUserAuthenticationEntity() {
    return userAuthenticationEntityBuilder().withId(null).withUsername("username")
        .withPassword("password");
  }

  public UserAuthenticationEntityBuilder withId(Long id) {
    entityWrapper.setId(id);
    return this;
  }

  public UserAuthenticationEntityBuilder withUsername(String username) {
    entityWrapper.setUsername(username);
    return this;
  }

  public UserAuthenticationEntityBuilder withPassword(String password) {
    entityWrapper.setPassword(password);
    return this;
  }
}
