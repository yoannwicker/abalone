package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.SquarePosition.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record Pawn(Player player, SquarePosition squarePosition) {

  private static final Set<SquarePosition> BLACK_PAWN_INITIAL_POSITIONS =
      Set.of(A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, B6, C3, C4, C5);

  private static final Set<SquarePosition> WHITE_PAWN_INITIAL_POSITIONS =
      Set.of(I5, I6, I7, I8, I9, H4, H5, H6, H7, H8, H9, G5, G6, G7);

  public static Set<Pawn> createBlackPawns() {
    return BLACK_PAWN_INITIAL_POSITIONS.stream()
        .map(squarePosition -> new Pawn(Player.BLACK, squarePosition))
        .collect(Collectors.toSet());
  }

  public static Set<Pawn> createWhitePawns() {
    return WHITE_PAWN_INITIAL_POSITIONS.stream()
        .map(squarePosition -> new Pawn(Player.WHITE, squarePosition))
        .collect(Collectors.toSet());
  }

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
}
