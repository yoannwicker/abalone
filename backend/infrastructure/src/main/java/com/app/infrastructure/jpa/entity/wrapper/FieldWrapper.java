package com.app.infrastructure.jpa.entity.wrapper;

import java.lang.reflect.Field;

public final class FieldWrapper<T> {

  private final Object entity;
  private final Field field;

  public FieldWrapper(Object entity, String fieldName) {
    try {
      this.entity = entity;
      var field = entity.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      this.field = field;
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public T get() {
    try {
      return (T) field.get(entity);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void set(Object value) {
    try {
      field.set(entity, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
