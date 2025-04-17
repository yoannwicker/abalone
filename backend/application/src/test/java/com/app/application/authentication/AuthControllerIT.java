package com.app.application.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.application.authentication.dto.AuthenticationRequest;
import com.app.application.authentication.dto.RegisterRequest;
import com.app.domain.auth.usecase.GenerateToken;
import com.app.domain.auth.usecase.SaveUserAuthentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private UserDetailsServiceImpl userDetailsService;

  @MockBean
  private SaveUserAuthentication saveUserAuthentication;

  @MockBean
  private GenerateToken generateToken;

  @Test
  public void should_register_user() throws Exception {
    // given
    RegisterRequest registerRequest = new RegisterRequest("username", "password");

    // when then
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(registerRequest)))
        .andExpect(status().isOk());
    verify(saveUserAuthentication).save(Mockito.any());
  }

  @Test
  public void should_build_token() throws Exception {
    // given
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");
    UserDetails userDetails = new User("username", "password", Set.of(() -> "ROLE_USER"));
    String mockJwtToken = "mockJwtToken";

    when(authenticationManager.authenticate(Mockito.any()))
        .thenReturn(new UsernamePasswordAuthenticationToken(authenticationRequest.username(),
            authenticationRequest.password()));

    when(userDetailsService.loadUserByUsername(authenticationRequest.username()))
        .thenReturn(userDetails);

    when(generateToken.execute(authenticationRequest.username()))
        .thenReturn(mockJwtToken);

    // when then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(authenticationRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(mockJwtToken));
  }

  private String asJsonString(Object object) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(object);
  }
}
