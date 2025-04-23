package com.app.domain.abalone.game.model;

public enum Player {
  BLACK,
  WHITE;

  Player nextPlayer() {
    if (this == BLACK) {
      return WHITE;
    }
    return BLACK;
  }
}
