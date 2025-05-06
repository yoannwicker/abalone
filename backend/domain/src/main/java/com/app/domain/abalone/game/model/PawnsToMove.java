package com.app.domain.abalone.game.model;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record PawnsToMove(Set<Pawn> pawns, Direction direction, boolean isLateralMove) {
  public PawnsToMove {
    if (pawns == null) {
      throw new IllegalArgumentException("Pawns cannot be null");
    }
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
  }

  public Set<Pawn> futurePawns() {
    return pawns.stream()
        .map(pawn -> pawn.projected(direction))
        .flatMap(Optional::stream)
        .collect(Collectors.toSet());
  }

  public boolean overlapFuturePositions(Pawn pawn) {
    if (isLateralMove) {
      if (futurePawns().stream().anyMatch(pawn::hasSamePositionOf)) {
        throw new IllegalStateException("Cannot move sideways to occupied square");
      }
      return false;
    }
    return getPawnInFrontProjection().map(pawn::hasSamePositionOf).orElse(false);
  }

  public Optional<Pawn> getPawnInFrontProjection() {
    return getPawnInFront().projected(direction);
  }

  private Pawn getPawnInFront() {
    if (isLateralMove) {
      throw new IllegalStateException("Cannot select pawn in front of a lateral move");
    }
    return pawns.stream()
        .filter(
            pawn ->
                pawn.projected(direction)
                    .map(pawnProjection -> !pawns.contains(pawnProjection))
                    .orElse(true))
        .findAny()
        .orElseThrow();
  }

  public boolean isOpponentOverlapFuturePositions(Pawn pawn) {
    return getPawnInFrontProjection().map(Pawn::toOpponentPawn).map(pawn::equals).orElse(false);
  }

  public OpponentPawnsToPush pawnsToPushProjection() {
    if (isLateralMove) {
      throw new IllegalStateException("Cannot push pawn in front of a lateral move");
    }
    if (pawns.size() < 2) {
      throw new IllegalStateException("Cannot push pawn with less than 2 pawns");
    }

    Optional<Pawn> firstPotentialPawnToPush = getPawnInFrontProjection().map(Pawn::toOpponentPawn);

    return firstPotentialPawnToPush
        .map(
            firstPawn -> {
              if (pawns.size() > 2) {
                Optional<Pawn> secondPotentialPawnToPush = firstPawn.projected(direction());
                return secondPotentialPawnToPush
                    .map(secondPawn -> new OpponentPawnsToPush(firstPawn, secondPawn, direction))
                    .orElse(new OpponentPawnsToPush(firstPawn, direction));
              }
              return new OpponentPawnsToPush(firstPawn, direction);
            })
        .orElse(OpponentPawnsToPush.empty());
  }
}
