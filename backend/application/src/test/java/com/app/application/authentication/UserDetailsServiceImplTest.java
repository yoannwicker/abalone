package com.app.application.authentication;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.usecase.FindUserAuthentication;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Mock
  private FindUserAuthentication findUserAuthentication;

  @Test
  void should_load_user() {
    // given
    String username = "username";
    UserAuthentication userAuthentication = new UserAuthentication(username, "password");
    when(findUserAuthentication.byUsername(username)).thenReturn(of(userAuthentication));

    // when
    UserDetails result = userDetailsService.loadUserByUsername(username);

    // then
    GrantedAuthority expectedAuthority = () -> "ROLE_USER";
    assertThat(result)
        .extracting(UserDetails::getUsername, UserDetails::getPassword, UserDetails::getAuthorities)
        .contains(username, "password", Set.of(expectedAuthority));
  }

  @Test
  void should_not_load_user_when_not_found_user() {
    // given
    String username = "username";
    when(findUserAuthentication.byUsername(username)).thenReturn(empty());

    // when then
    assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("User not found");
  }
}
