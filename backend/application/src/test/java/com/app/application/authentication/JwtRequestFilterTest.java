package com.app.application.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.domain.auth.usecase.ManageToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JwtRequestFilterTest {

  @InjectMocks
  private JwtRequestFilter jwtRequestFilter;

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private ManageToken manageToken;

  @Mock
  private FilterChain filterChain;

  @BeforeEach
  public void setUp() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  @Test
  public void should_do_filter_internal() throws ServletException, IOException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    String token = "dummyToken";
    String username = "testuser";

    request.addHeader("Authorization", "Bearer " + token);

    when(manageToken.extractUsername(token)).thenReturn(username);
    UserDetails userDetails = User.withUsername(username).password("password").roles("USER")
        .build();
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
    when(manageToken.validate(token, username)).thenReturn(true);

    // when
    jwtRequestFilter.doFilterInternal(request, response, filterChain);

    // then
    assertThat(SecurityContextHolder.getContext().getAuthentication())
        .extracting(Authentication::getPrincipal)
        .isEqualTo(userDetails);
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  public void should_do_filter_internal_when_invalid_token() throws ServletException, IOException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    String token = "invalidToken";
    String username = "testuser";

    request.addHeader("Authorization", "Bearer " + token);

    when(manageToken.extractUsername(token)).thenReturn(username);
    when(manageToken.validate(token, username)).thenReturn(false);
    UserDetails userDetails = User.withUsername(username).password("password").roles("USER")
        .build();
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

    // when
    jwtRequestFilter.doFilterInternal(request, response, filterChain);

    // then
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  public void should_throw_error_when_user_not_found() throws ServletException, IOException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    String token = "dummyToken";
    String username = "unknownuser";

    request.addHeader("Authorization", "Bearer " + token);

    when(manageToken.extractUsername(token)).thenReturn(username);
    when(userDetailsService.loadUserByUsername(username)).thenThrow(
        new UsernameNotFoundException("User not found"));

    // when then
    assertThatThrownBy(() -> jwtRequestFilter.doFilterInternal(request, response, filterChain))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("User not found");

    // then
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain, times(0)).doFilter(request, response);
  }
}
