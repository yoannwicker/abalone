package com.app.domain.abalone.game.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public enum Direction {
  MOVE_FORWARD_X(SquarePosition::nextInXAxis),
  MOVE_FORWARD_Y(SquarePosition::nextInYAxis),
  MOVE_FORWARD_Z(SquarePosition::nextInZAxis),
  MOVE_BACK_X(SquarePosition::previousInXAxis),
  MOVE_BACK_Y(SquarePosition::previousInYAxis),
  MOVE_BACK_Z(SquarePosition::previousInZAxis);

  private final Function<SquarePosition, Optional<SquarePosition>> nextPositionFunction;

  Direction(Function<SquarePosition, Optional<SquarePosition>> nextPositionFunction) {
    this.nextPositionFunction = nextPositionFunction;
  }

  private static Optional<Direction> findDirectionTo(
      SquarePosition firstPosition, SquarePosition secondPosition) {
    return Arrays.stream(values())
        .filter(direction -> secondPosition.equals(direction.next(firstPosition).orElse(null)))
        .findFirst();
  }

  public static Direction directionOf(SquarePosition firstPosition, SquarePosition secondPosition) {
    return findDirectionTo(firstPosition, secondPosition)
        .orElseThrow(() -> new IllegalArgumentException("The next position is not adjacent"));
  }

  public static boolean isAdjacent(SquarePosition firstPosition, SquarePosition secondPosition) {
    return findDirectionTo(firstPosition, secondPosition).isPresent();
  }

  public Optional<SquarePosition> next(SquarePosition squarePosition) {
    return nextPositionFunction.apply(squarePosition);
  }
}
