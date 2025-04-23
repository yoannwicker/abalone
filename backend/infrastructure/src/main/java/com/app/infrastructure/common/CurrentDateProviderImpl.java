package com.app.infrastructure.common;

import java.util.Date;
import java.util.function.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public class CurrentDateProviderImpl implements Supplier<Date> {

  @Override
  public Date get() {
    return new Date();
  }
}
