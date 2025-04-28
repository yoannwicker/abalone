package com.app.domain.abalone.game.model;

import java.util.Optional;

public enum BoardLine {
  A,
  B,
  C,
  D,
  E,
  F,
  G,
  H,
  I;

  public Optional<BoardLine> next() {
    int index = this.ordinal();
    if (index == values().length - 1) {
      return Optional.empty();
    }
    return Optional.of(values()[index + 1]);
  }

  public Optional<BoardLine> previous() {
    int index = this.ordinal();
    if (index == 0) {
      return Optional.empty();
    }
    return Optional.of(values()[index - 1]);
  }

  public boolean inBottomTheBoard() {
    return this.ordinal() < 4;
  }

  public boolean inAboveTheBoard() {
    return this.ordinal() > 4;
  }
}
