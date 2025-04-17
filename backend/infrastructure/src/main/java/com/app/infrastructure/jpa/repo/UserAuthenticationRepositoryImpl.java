package com.app.infrastructure.jpa.repo;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.repository.UserAuthenticationRepository;
import com.app.infrastructure.jpa.entity.UserAuthenticationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<UserAuthentication> findByUsername(String username) {
    return entityManager.createQuery("SELECT u FROM UserAuthenticationEntity u WHERE u.username = :username", UserAuthenticationEntity.class)
        .setParameter("username", username)
        .getResultStream()
        .map(UserAuthenticationEntity::toDomain)
        .findAny();
  }

  @Override
  public void save(UserAuthentication user) {
    var userEntity = UserAuthenticationEntity.fromDomain(user);
    if (userEntity.getId() == null) {
      entityManager.persist(userEntity);
    } else {
      entityManager.merge(userEntity);
    }
  }
}

