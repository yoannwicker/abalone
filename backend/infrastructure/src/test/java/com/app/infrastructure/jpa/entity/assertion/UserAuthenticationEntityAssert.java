package com.app.infrastructure.jpa.entity.assertion;

import com.app.infrastructure.jpa.entity.UserAuthenticationEntity;
import com.app.infrastructure.jpa.entity.wrapper.UserAuthenticationEntityWrapper;
import org.assertj.core.api.AbstractAssert;

public class UserAuthenticationEntityAssert extends
    AbstractAssert<UserAuthenticationEntityAssert, UserAuthenticationEntity> {

  public UserAuthenticationEntityAssert(UserAuthenticationEntity actual) {
    super(actual, UserAuthenticationEntityAssert.class);
  }

  public static UserAuthenticationEntityAssert assertThat(UserAuthenticationEntity actual) {
    return new UserAuthenticationEntityAssert(actual);
  }

  public UserAuthenticationEntityAssert isEqualTo(UserAuthenticationEntity expected) {
    isNotNull();

    var actualWrapper = new UserAuthenticationEntityWrapper(actual);
    var expectedWrapper = new UserAuthenticationEntityWrapper(expected);

    if (!actualWrapper.equals(expectedWrapper)) {
      failWithMessage("Expected animalAd to be equal to <%s> but was <%s>", expected, actual);
    }
    return this;
  }
}
