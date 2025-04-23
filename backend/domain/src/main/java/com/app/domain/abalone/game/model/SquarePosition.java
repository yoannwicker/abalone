package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.BoardLine.*;

import java.util.Arrays;
import java.util.Optional;

// TODO: renommer pour Position ?
public enum SquarePosition {
  A1(A, 1),
  B1(B, 1),
  C1(C, 1),
  D1(D, 1),
  E1(E, 1),
  F1(F, 1),
  G1(G, 1),
  H1(H, 1),
  I1(I, 1),
  A2(A, 2),
  B2(B, 2),
  C2(C, 2),
  D2(D, 2),
  E2(E, 2),
  F2(F, 2),
  G2(G, 2),
  H2(H, 2),
  I2(I, 2),
  A3(A, 3),
  B3(B, 3),
  C3(C, 3),
  D3(D, 3),
  E3(E, 3),
  F3(F, 3),
  G3(G, 3),
  H3(H, 3),
  I3(I, 3),
  A4(A, 4),
  B4(B, 4),
  C4(C, 4),
  D4(D, 4),
  E4(E, 4),
  F4(F, 4),
  G4(G, 4),
  H4(H, 4),
  I4(I, 4),
  A5(A, 5),
  B5(B, 5),
  C5(C, 5),
  D5(D, 5),
  E5(E, 5),
  F5(F, 5),
  G5(G, 5),
  H5(H, 5),
  I5(I, 5),
  B6(B, 6),
  C6(C, 6),
  D6(D, 6),
  E6(E, 6),
  F6(F, 6),
  G6(G, 6),
  H6(H, 6),
  C7(C, 7),
  D7(D, 7),
  E7(E, 7),
  F7(F, 7),
  G7(G, 7),
  D8(D, 8),
  E8(E, 8),
  F8(F, 8),
  E9(E, 9);

  private final BoardLine line;
  private final int number;

  SquarePosition(BoardLine line, int number) {
    this.line = line;
    this.number = number;
  }

  public static Optional<SquarePosition> from(Optional<BoardLine> lineOptional, int number) {
    return Arrays.stream(SquarePosition.values())
        .filter(position -> position.number == number)
        .filter(
            position ->
                lineOptional.map(character -> character.equals(position.line)).orElse(false))
        .findAny();
  }

  public boolean isSideTo(SquarePosition squarePosition) {
    return Math.abs(number - squarePosition.number) <= 1
        && Math.abs(line.ordinal() - squarePosition.line.ordinal()) <= 1;
  }

  public Optional<SquarePosition> next(Direction direction) {
    return switch (direction) {
      case MOVE_FORWARD_X -> from(Optional.of(line), number + 1);
      case MOVE_BACK_X -> from(Optional.of(line), number - 1);
      case MOVE_FORWARD_Y -> from(line.next(), line.inAboveTheBoard() ? number + 1 : number);
      case MOVE_BACK_Y -> from(line.previous(), line.inBottomTheBoard() ? number : number - 1);
      case MOVE_FORWARD_Z -> from(line.next(), line.inAboveTheBoard() ? number : number - 1);
      case MOVE_BACK_Z -> from(line.previous(), line.inBottomTheBoard() ? number + 1 : number);
    };
  }

  public Direction directionTo(SquarePosition nextPosition) {
    if (nextPosition.line == line) {
      return nextPosition.number > number ? Direction.MOVE_FORWARD_X : Direction.MOVE_BACK_X;
    }

    if (line.next().orElse(null) == nextPosition.line) {
      return nextPosition.number > number ? Direction.MOVE_FORWARD_Y : Direction.MOVE_FORWARD_Z;
    }

    return nextPosition.number > number ? Direction.MOVE_BACK_Z : Direction.MOVE_BACK_Y;
  }
}
