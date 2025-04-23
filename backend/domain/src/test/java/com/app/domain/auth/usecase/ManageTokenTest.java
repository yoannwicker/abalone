package com.app.domain.auth.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.app.domain.auth.entity.AuthenticationInformation;
import com.app.domain.auth.repository.TokenRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ManageTokenTest {

  private static final Instant BEFORE_CURRENT =
      LocalDateTime.of(2024, 6, 20, 10, 0, 0).atZone(ZoneId.systemDefault()).toInstant();
  private static final Instant CURRENT =
      LocalDateTime.of(2024, 7, 20, 10, 0, 0).atZone(ZoneId.systemDefault()).toInstant();
  private static final Instant AFTER_CURRENT =
      LocalDateTime.of(2024, 8, 20, 10, 0, 0).atZone(ZoneId.systemDefault()).toInstant();
  @Mock private TokenRepository tokenRepository;
  private ManageToken manageToken;

  static Stream<Arguments> tokenValidationArguments() {
    return Stream.of(
        // Test case: Token is valid
        Arguments.of(Date.from(AFTER_CURRENT), "username", true),
        // Test case: Token is expired
        Arguments.of(Date.from(BEFORE_CURRENT), "username", false),
        // Test case: Username does not match
        Arguments.of(Date.from(AFTER_CURRENT), "an-other-username", false));
  }

  @BeforeEach
  void setUp() {
    manageToken = new ManageToken(tokenRepository, () -> Date.from(CURRENT));
  }

  @ParameterizedTest
  @MethodSource("tokenValidationArguments")
  void should_validate_token(Date expirationDate, String inputUsername, boolean expectedResult) {
    // given
    String token = "token";
    String username = "username";
    when(tokenRepository.extractAuthenticationInformations(
            eq(token), argThat(claimsResolverMatcher(username, expirationDate))))
        .thenAnswer(
            invocation -> {
              Function<AuthenticationInformation, ?> claimsResolver = invocation.getArgument(1);
              AuthenticationInformation authInfo =
                  new AuthenticationInformation(username, expirationDate);
              return claimsResolver.apply(authInfo);
            });

    // when then
    assertThat(manageToken.validate(token, inputUsername)).isEqualTo(expectedResult);
  }

  @Test
  void should_extract_username() {
    // given
    String token = "token";
    String username = "username";

    when(tokenRepository.extractAuthenticationInformations(
            eq(token), argThat(claimsResolverMatcher(username))))
        .thenAnswer(
            invocation -> {
              Function<AuthenticationInformation, ?> claimsResolver = invocation.getArgument(1);
              AuthenticationInformation authInfo = new AuthenticationInformation(username, null);
              return claimsResolver.apply(authInfo);
            });

    // when
    String result = manageToken.extractUsername(token);

    // then
    String expectedUsername = "username";
    assertThat(result).isEqualTo(expectedUsername);
  }

  private ArgumentMatcher<Function<AuthenticationInformation, ?>> claimsResolverMatcher(
      String username) {
    return claimsResolverMatcher(username, null);
  }

  private ArgumentMatcher<Function<AuthenticationInformation, ?>> claimsResolverMatcher(
      String username, Date expirationDate) {
    return argument -> {
      AuthenticationInformation authInfo = new AuthenticationInformation(username, expirationDate);
      return argument.apply(authInfo).equals(username)
          || argument.apply(authInfo).equals(expirationDate);
    };
  }
}
