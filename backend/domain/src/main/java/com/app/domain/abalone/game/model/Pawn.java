package com.app.domain.abalone.game.model;

import java.util.Optional;

public record Pawn(Player player, SquarePosition squarePosition) {

  public Optional<Pawn> projected(Direction direction) {
    return squarePosition.next(direction).map(squarePosition -> new Pawn(player, squarePosition));
  }

  public Pawn toOpponentPawn() {
    return new Pawn(player.nextPlayer(), squarePosition);
  }

  public boolean isAdjacentTo(Pawn pawn) {
    return squarePosition.isAdjacent(pawn.squarePosition);
  }

  public boolean hasProjection(Direction direction, Pawn pawnProjection) {
    return squarePosition
        .next(direction)
        .map(position -> new Pawn(player, position))
        .map(pawnProjection::equals)
        .orElse(true);
  }

  public Direction directionTo(Pawn pawnProjection) {
    return this.squarePosition.directionTo(pawnProjection.squarePosition());
  }

  public boolean hasSamePositionOf(Pawn pawn) {
    return this.squarePosition.equals(pawn.squarePosition());
  }
}
