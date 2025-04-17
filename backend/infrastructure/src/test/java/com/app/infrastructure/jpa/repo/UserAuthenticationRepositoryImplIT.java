package com.app.infrastructure.jpa.repo;

import static com.app.infrastructure.jpa.entity.builder.UserAuthenticationEntityBuilder.defaultUserAuthenticationEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.repository.UserAuthenticationRepository;
import com.app.infrastructure.jpa.DatasourceConfig;
import com.app.infrastructure.jpa.JpaConfig;
import com.app.infrastructure.jpa.entity.UserAuthenticationEntity;
import com.app.infrastructure.jpa.entity.assertion.UserAuthenticationEntityAssert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    DatasourceConfig.class,
    JpaConfig.class,
    UserAuthenticationRepositoryImplIT.TestConfiguration.class})
@SpringBootTest
@Transactional
public class UserAuthenticationRepositoryImplIT {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private UserAuthenticationRepository userAuthenticationRepository;

  @Test
  public void should_find_by_username() {
    // given
    UserAuthenticationEntity userEntity = defaultUserAuthenticationEntity()
        .withId(null)
        .withUsername("username")
        .withPassword("password")
        .build();
    entityManager.persist(userEntity);

    // when
    Optional<UserAuthentication> result = userAuthenticationRepository.findByUsername("username");

    // then
    UserAuthentication expectedUser = new UserAuthentication("username", "password");
    assertThat(result).contains(expectedUser);
  }

  @Test
  public void should_save_user() {
    // given
    UserAuthentication user = new UserAuthentication("username", "password");

    // when
    userAuthenticationRepository.save(user);
    UserAuthenticationEntity result = entityManager
        .find(UserAuthenticationEntity.class, 1L);

    // then
    UserAuthenticationEntity expectedUserEntity = defaultUserAuthenticationEntity()
        .withId(1L)
        .withUsername("username")
        .withPassword("password")
        .build();
    UserAuthenticationEntityAssert.assertThat(result).isEqualTo(expectedUserEntity);
  }

  @Configuration
  @PropertySource({"/integration-test.properties"})
  static class TestConfiguration {

    @Bean
    UserAuthenticationRepository userAuthenticationRepository() {
      return new UserAuthenticationRepositoryImpl();
    }
  }
}
