package com.app.domain.auth.usecase;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.repository.UserAuthenticationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveUserAuthenticationTest {

  @Mock
  private UserAuthenticationRepository userAuthenticationRepository;

  @InjectMocks
  private SaveUserAuthentication saveUserAuthentication;

  @Test
  void should_save_user_authentication() {
    // given
    var userAuthentication = new UserAuthentication("username", "password");
    when(userAuthenticationRepository.findByUsername(userAuthentication.username())).thenReturn(
        empty());

    // when
    saveUserAuthentication.save(userAuthentication);

    // then
    verify(userAuthenticationRepository).save(userAuthentication);
  }

  @Test
  void should_throw_exception_when_user_already_exists() {
    // given
    var userAuthentication = new UserAuthentication("username", "password");
    when(userAuthenticationRepository.findByUsername(userAuthentication.username())).thenReturn(
        of(userAuthentication));

    // when then
    Assertions.assertThatThrownBy(() -> saveUserAuthentication.save(userAuthentication))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("User already exists");
  }
}
