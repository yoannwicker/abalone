package com.app.domain.abalone.game.model;

import java.util.Optional;

public record GameStatus(boolean end, Optional<Player> winner) {

  static final int MAXIMUM_NUMBER_OF_LOST_PAWNS = 6;

  public GameStatus(int blackPawnsLost, int whitePawnsLost) {
    this(isEnd(blackPawnsLost, whitePawnsLost), computeWinner(blackPawnsLost, whitePawnsLost));
  }

  public static GameStatus inProgress() {
    return new GameStatus(false, Optional.empty());
  }

  public static GameStatus draw() {
    return new GameStatus(true, Optional.empty());
  }

  private static boolean isEnd(int blackPawnsLost, int whitePawnsLost) {
    return blackPawnsLost == MAXIMUM_NUMBER_OF_LOST_PAWNS
        || whitePawnsLost == MAXIMUM_NUMBER_OF_LOST_PAWNS;
  }

  private static Optional<Player> computeWinner(int blackPawnsLost, int whitePawnsLost) {
    if (blackPawnsLost == MAXIMUM_NUMBER_OF_LOST_PAWNS) {
      return Optional.of(Player.WHITE);
    }
    if (whitePawnsLost == MAXIMUM_NUMBER_OF_LOST_PAWNS) {
      return Optional.of(Player.BLACK);
    }
    return Optional.empty();
  }
}
