package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.Direction.MOVE_BACK_Z;
import static com.app.domain.abalone.game.model.Direction.MOVE_FORWARD_X;
import static com.app.domain.abalone.game.model.Direction.MOVE_FORWARD_Y;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Move {

  private final Set<Pawn> pawns;
  private final Direction direction;
  private final boolean isSideways;

  public Move(Set<Pawn> pawns, Direction direction) {
    if (pawns.isEmpty()) {
      throw new IllegalArgumentException("No pawns given");
    }
    if (pawns.size() > 3) {
      throw new IllegalArgumentException("Cannot move pawns with more than 3 pawns");
    }
    if (!allPawnsHaveSameColor(pawns)) {
      throw new IllegalArgumentException("Cannot move pawns with different colors");
    }
    this.pawns = pawns;
    this.direction = direction;
    if (!allPawnsFollowedLineInAnyDirection(pawns)) {
      throw new IllegalArgumentException("Cannot move not followed pawns");
    }
    this.isSideways = !allPawnsFollowedInLineOfDirection(pawns, direction);
  }

  public Move(Pawn pawn, Direction direction) {
    this(Set.of(pawn), direction);
  }

  private static boolean allPawnsHaveSameColor(Set<Pawn> pawnsToMove) {
    var aPawns = pawnsToMove.stream().findAny().orElseThrow();
    return pawnsToMove.stream().allMatch(pawn -> aPawns.player().equals(pawn.player()));
  }

  private static boolean allPawnsFollowedLineInAnyDirection(Set<Pawn> pawnsToMove) {
    if (pawnsToMove.size() > 1) {
      List<Pawn> sortedPawnsToMove =
          pawnsToMove.stream().sorted(Comparator.comparing(Pawn::squarePosition)).toList();
      Pawn firstPawn = sortedPawnsToMove.get(0);
      Pawn secondPawn = sortedPawnsToMove.get(1);
      boolean areFirstTwoPawnsAdjacent = firstPawn.isSideTo(secondPawn);
      if (pawnsToMove.size() > 2) {
        Pawn thirdPawn = sortedPawnsToMove.get(2);
        var direction = firstPawn.directionTo(secondPawn);
        return secondPawn.hasProjection(direction, thirdPawn);
      }
      return areFirstTwoPawnsAdjacent;
    }
    return true;
  }

  private static boolean allPawnsFollowedInLineOfDirection(
      Set<Pawn> pawnsToMove, Direction direction) {
    if (pawnsToMove.size() > 1) {
      List<Pawn> sortedPawnsToMove = getSortedPawnsToMove(pawnsToMove, direction);
      return IntStream.range(0, sortedPawnsToMove.size() - 1)
          .allMatch(
              i -> sortedPawnsToMove.get(i).hasProjection(direction, sortedPawnsToMove.get(i + 1)));
    }
    return true;
  }

  private static List<Pawn> getSortedPawnsToMove(Set<Pawn> pawnsToMove, Direction direction) {
    if (Set.of(MOVE_FORWARD_X, MOVE_FORWARD_Y, MOVE_BACK_Z).contains(direction)) {
      return pawnsToMove.stream().sorted(Comparator.comparing(Pawn::squarePosition)).toList();
    }
    return pawnsToMove.stream()
        .sorted(Comparator.comparing(Pawn::squarePosition).reversed())
        .toList();
  }

  public Set<Pawn> movePawns() {
    return pawns.stream()
        .map(pawn -> pawn.projected(direction))
        .flatMap(Optional::stream)
        .collect(Collectors.toSet());
  }

  public Pawn getPawnInFront() {
    return pawns.stream()
        .filter(
            pawn ->
                pawn.projected(direction)
                    .map(pawnProjection -> !pawns.contains(pawnProjection))
                    .orElse(true))
        .findAny()
        .orElseThrow();
  }

  public Set<Pawn> pawns() {
    return pawns;
  }

  public Direction direction() {
    return direction;
  }

  public boolean isSideways() {
    return isSideways;
  }
}
