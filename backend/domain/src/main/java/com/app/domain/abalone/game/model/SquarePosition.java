package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.BoardLine.*;
import static com.app.domain.abalone.game.model.Direction.directionOf;

import java.util.Arrays;
import java.util.Optional;

public enum SquarePosition {
  A1(A, 1),
  A2(A, 2),
  A3(A, 3),
  A4(A, 4),
  A5(A, 5),
  B1(B, 1),
  B2(B, 2),
  B3(B, 3),
  B4(B, 4),
  B5(B, 5),
  B6(B, 6),
  C1(C, 1),
  C2(C, 2),
  C3(C, 3),
  C4(C, 4),
  C5(C, 5),
  C6(C, 6),
  C7(C, 7),
  D1(D, 1),
  D2(D, 2),
  D3(D, 3),
  D4(D, 4),
  D5(D, 5),
  D6(D, 6),
  D7(D, 7),
  D8(D, 8),
  E1(E, 1),
  E2(E, 2),
  E3(E, 3),
  E4(E, 4),
  E5(E, 5),
  E6(E, 6),
  E7(E, 7),
  E8(E, 8),
  E9(E, 9),
  F2(F, 2),
  F3(F, 3),
  F4(F, 4),
  F5(F, 5),
  F6(F, 6),
  F7(F, 7),
  F8(F, 8),
  F9(F, 9),
  G3(G, 3),
  G4(G, 4),
  G5(G, 5),
  G6(G, 6),
  G7(G, 7),
  G8(G, 8),
  G9(G, 9),
  H4(H, 4),
  H5(H, 5),
  H6(H, 6),
  H7(H, 7),
  H8(H, 8),
  H9(H, 9),
  I5(I, 5),
  I6(I, 6),
  I7(I, 7),
  I8(I, 8),
  I9(I, 9);

  private final BoardLine line;
  private final int number;

  SquarePosition(BoardLine line, int number) {
    this.line = line;
    this.number = number;
  }

  private static Optional<SquarePosition> fromName(String positionName) {
    return Arrays.stream(SquarePosition.values())
        .filter(position -> position.name().equalsIgnoreCase(positionName))
        .findAny();
  }

  public Optional<SquarePosition> nextInXAxis() {
    return fromName(line.name() + (number + 1));
  }

  public Optional<SquarePosition> previousInXAxis() {
    return fromName(line.name() + (number - 1));
  }

  public Optional<SquarePosition> nextInYAxis() {
    return line.next().flatMap(l -> fromName(l.name() + (number + 1)));
  }

  public Optional<SquarePosition> previousInYAxis() {
    return line.previous().flatMap(l -> fromName(l.name() + (number - 1)));
  }

  public Optional<SquarePosition> nextInZAxis() {
    return line.next().flatMap(l -> fromName(l.name() + number));
  }

  public Optional<SquarePosition> previousInZAxis() {
    return line.previous().flatMap(l -> fromName(l.name() + number));
  }

  public boolean isAdjacent(SquarePosition squarePosition) {
    return Direction.isAdjacent(this, squarePosition);
  }

  public Optional<SquarePosition> next(Direction direction) {
    return direction.next(this);
  }

  public Direction directionTo(SquarePosition nextPosition) {
    return directionOf(this, nextPosition);
  }
}
