package com.app.infrastructure.jjwt;

import com.app.domain.auth.entity.AuthenticationInformation;
import com.app.domain.auth.repository.TokenParameterProvider;
import com.app.domain.auth.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.xml.bind.DatatypeConverter;

public record TokenJjwtRepository(TokenParameterProvider tokenParameterProvider) implements TokenRepository {

  @Override
  public <T> T extractAuthenticationInformations(String token,
      Function<AuthenticationInformation, T> authenticationInformationResolver) {
    final AuthenticationInformation authenticationInformation = extractAllAuthenticationInformations(token);
    return authenticationInformationResolver.apply(authenticationInformation);
  }

  private AuthenticationInformation extractAllAuthenticationInformations(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(tokenParameterProvider.secret()))
        .parseClaimsJws(token)
        .getBody();
    return new AuthenticationInformation(claims.getSubject(), claims.getExpiration());
  }

  @Override
  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(
            new Date(System.currentTimeMillis() + tokenParameterProvider.jwtExpirationInMillis()))
        .signWith(SignatureAlgorithm.HS256,
            DatatypeConverter.parseBase64Binary(tokenParameterProvider.secret()))
        .compact();
  }
}
