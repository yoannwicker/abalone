package com.app.application;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.application.authentication.UserDetailsServiceImpl;
import com.app.domain.auth.usecase.ManageToken;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityFilterChainIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserDetailsServiceImpl userDetailsService;

  @MockBean
  private ManageToken manageToken;

  @RestController
  @RequestMapping("/api/auth")
  public static class ApiAuthController {

    @GetMapping("/endpoint")
    public ResponseEntity<?> getEndpoint() {
      return ResponseEntity.ok().build();
    }
  }

  @RestController
  @RequestMapping("/api")
  public static class ApiController {

    @GetMapping("/endpoint")
    public ResponseEntity<?> getEndpoint() {
      return ResponseEntity.ok().build();
    }
  }

  @Test
  public void should_access_authentication_api_when_user_is_not_authenticated() throws Exception {
    mockMvc.perform(get("/api/auth/endpoint"))
        .andExpect(status().isOk());
  }

  @Test
  public void should_deny_access_to_another_api_when_user_is_not_authenticated() throws Exception {
    // given
    SecurityContextHolder.clearContext();

    // when then
    mockMvc.perform(get("/api/endpoint"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void should_access_to_another_api_when_user_is_authenticated() throws Exception {
    // given
    String username = "username";

    UserDetails userDetails = new User(username, "password", Set.of(() -> "ROLE_USER"));
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

    // when then
    mockMvc.perform(get("/api/endpoint"))
        .andExpect(status().isOk());
  }

  @Test
  public void should_access_to_another_api_when_request_contains_authentication() throws Exception {
    // given
    String token = "token";
    String username = "username";
    SecurityContextHolder.clearContext();

    when(manageToken.extractUsername(token)).thenReturn("username");
    when(userDetailsService.loadUserByUsername("username"))
        .thenReturn(new User("username", "password", Set.of(() -> "ROLE_USER")));
    when(manageToken.validate(token, username)).thenReturn(true);

    // when then
    mockMvc.perform(get("/api/endpoint")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }
}

