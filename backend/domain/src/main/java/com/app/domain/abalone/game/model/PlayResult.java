package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.GameStatus.MAXIMUM_NUMBER_OF_LOST_PAWNS;

import java.util.Set;

public record PlayResult(
    Set<Pawn> movedPawns, int blackPawnsLost, int whitePawnsLost, GameStatus gameStatus) {

  public PlayResult(Set<Pawn> movedPawns, int blackPawnsLost, int whitePawnsLost) {
    this(
        movedPawns, blackPawnsLost, whitePawnsLost, new GameStatus(blackPawnsLost, whitePawnsLost));
  }

  public PlayResult {
    if (blackPawnsLost == MAXIMUM_NUMBER_OF_LOST_PAWNS
        && whitePawnsLost == MAXIMUM_NUMBER_OF_LOST_PAWNS) {
      throw new IllegalStateException();
    }
    if (blackPawnsLost > MAXIMUM_NUMBER_OF_LOST_PAWNS
        || whitePawnsLost > MAXIMUM_NUMBER_OF_LOST_PAWNS) {
      throw new IllegalStateException();
    }
  }
}
