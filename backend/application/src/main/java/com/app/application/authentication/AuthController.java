package com.app.application.authentication;

import com.app.application.authentication.dto.AuthenticationRequest;
import com.app.application.authentication.dto.AuthenticationResponse;
import com.app.application.authentication.dto.RegisterRequest;
import com.app.domain.auth.usecase.GenerateToken;
import com.app.domain.auth.usecase.SaveUserAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final UserDetailsServiceImpl userDetailsService;

  private final SaveUserAuthentication saveUserAuthentication;

  private final PasswordEncoder passwordEncoder;

  private final GenerateToken generateToken;

  public AuthController(AuthenticationManager authenticationManager,
      UserDetailsServiceImpl userDetailsService, SaveUserAuthentication saveUserAuthentication,
      PasswordEncoder passwordEncoder, GenerateToken generateToken) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.saveUserAuthentication = saveUserAuthentication;
    this.passwordEncoder = passwordEncoder;
    this.generateToken = generateToken;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    saveUserAuthentication.save(
        registerRequest.toDomain(passwordEncoder.encode(registerRequest.password())));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.username(),
              authenticationRequest.password())
      );
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(
        authenticationRequest.username());

    final String jwt = generateToken.execute(userDetails.getUsername());

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
