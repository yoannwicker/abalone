package com.app.infrastructure.common;

import com.app.domain.common.repository.CurrentDateProvider;
import java.util.Date;
import org.springframework.stereotype.Repository;

@Repository
public class CurrentDateProviderImpl implements CurrentDateProvider {

  @Override
  public Date getCurrentDate() {
    return new Date();
  }
}
