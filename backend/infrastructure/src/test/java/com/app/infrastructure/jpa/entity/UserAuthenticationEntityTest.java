package com.app.infrastructure.jpa.entity;

import static com.app.infrastructure.jpa.entity.builder.UserAuthenticationEntityBuilder.defaultUserAuthenticationEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.auth.entity.UserAuthentication;

import com.app.infrastructure.jpa.entity.wrapper.UserAuthenticationEntityWrapper;
import org.junit.jupiter.api.Test;

class UserAuthenticationEntityTest {

  @Test
  void should_build_entity_from_domain() {
    // given
    var userAuthentication = new UserAuthentication("username", "password");

    // when
    UserAuthenticationEntity result = UserAuthenticationEntity.fromDomain(userAuthentication);

    // then
    var expectedEntity = defaultUserAuthenticationEntity().build();
    assertThat(new UserAuthenticationEntityWrapper(result))
            .isEqualTo(new UserAuthenticationEntityWrapper(expectedEntity));
  }

  @Test
  void should_convert_entity_to_domain() {
    // given
    var userAuthenticationEntity = defaultUserAuthenticationEntity().build();

    // when
    UserAuthentication result = userAuthenticationEntity.toDomain();

    // then
    var expectedUser = new UserAuthentication("username", "password");
    assertThat(result).isEqualTo(expectedUser);
  }
}
