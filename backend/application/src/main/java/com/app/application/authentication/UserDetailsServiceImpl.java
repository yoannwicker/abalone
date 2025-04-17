package com.app.application.authentication;

import com.app.domain.auth.entity.UserAuthentication;
import com.app.domain.auth.usecase.FindUserAuthentication;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private FindUserAuthentication findUserAuthentication;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserAuthentication> user = findUserAuthentication.byUsername(username);
    return user.map(this::buildUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  private UserDetails buildUserDetails(UserAuthentication user) {
    return org.springframework.security.core.userdetails.User.builder()
        .username(user.username())
        .password(user.password())
        .roles("USER")
        .build();
  }
}

