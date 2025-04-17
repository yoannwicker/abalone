package com.app.infrastructure.jpa.entity.builder;

public abstract class EntityBuilder<T> {

  abstract T entity();

  public T build() {
    return entity();
  }
}
