package com.app.infrastructure.jjwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.app.domain.auth.entity.AuthenticationInformation;
import com.app.domain.auth.repository.TokenParameterProvider;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenJjwtRepositoryTest {

  private static final String SECRET = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcxOTY0MTg5NCwiaWF0IjoxNzE5NjQxODk0fQ.Nk-iWSEiUs3XHh9xDn94E-XXwBo2qZS5ZP2CTb4Sj_s";

  @Nested
  class GenerateToken {

    @Mock
    private TokenParameterProvider tokenParameterProvider;

    @InjectMocks
    private TokenJjwtRepository tokenJjwtRepository;

    @Test
    void should_generate_token() {
      // Given
      when(tokenParameterProvider.secret()).thenReturn(SECRET);

      // When
      String result = tokenJjwtRepository.generateToken("username");

      // Then
      assertThat(result).isNotNull();
    }

    @Test
    void should_not_generate_token_with_empty_secret() {
      // Given
      String emptySecret = "";
      when(tokenParameterProvider.secret()).thenReturn(emptySecret);

      // When Then
      Assertions.assertThatThrownBy(() -> tokenJjwtRepository.generateToken("username"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("secret key byte array cannot be null or empty.");
    }
  }

  @Nested
  class ExtractAuthenticationInformation {

    private TokenJjwtRepository tokenJjwtRepository;

    @BeforeEach
    void setUp() {
      tokenJjwtRepository = new TokenJjwtRepository(new TokenParameterProvider() {
        @Override
        public String secret() {
          return SECRET;
        }

        @Override
        public long jwtExpirationInMillis() {
          return 3600;
        }
      });
    }

    @Test
    void should_extract_username() {
      // Given
      String username = "username";
      String token = tokenJjwtRepository.generateToken(username);

      // When
      String result = tokenJjwtRepository.extractAuthenticationInformations(token,
          AuthenticationInformation::username);

      // Then
      assertThat(result).isEqualTo(username);
    }

    @Test
    void should_extract_expiration() {
      // Given
      String username = "username";
      String token = tokenJjwtRepository.generateToken(username);

      // When
      Date result = tokenJjwtRepository.extractAuthenticationInformations(token,
          AuthenticationInformation::expiration);

      // Then
      assertThat(result).isAfter(new Date());
    }
  }

}
