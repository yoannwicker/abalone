package com.app.domain.abalone.game.model;

import static java.util.stream.Stream.concat;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OpponentPawnsToPush {
  private final Pawn firstPawn;
  private final Pawn secondPawn;
  private final Direction direction;
  private final int autorizedPawnsToPush;

  public OpponentPawnsToPush(
      Pawn firstPawn, Pawn secondPawn, Direction direction, int autorizedPawnsToPush) {
    this.firstPawn = firstPawn;
    this.secondPawn = secondPawn;
    this.direction = direction;
    this.autorizedPawnsToPush = autorizedPawnsToPush;
  }

  public OpponentPawnsToPush(Pawn firstPawn, Pawn secondPawn, Direction direction) {
    this(firstPawn, secondPawn, direction, 2);
  }

  public OpponentPawnsToPush(Pawn firstPawn, Direction direction) {
    this(firstPawn, null, direction, 1);
  }

  public static OpponentPawnsToPush empty() {
    return new OpponentPawnsToPush(null, null, null, 0);
  }

  public Optional<SquarePosition> nextSquareOfFirstPawnToPush() {
    return Optional.ofNullable(firstPawn)
        .flatMap(pawn -> pawn.projected(direction))
        .map(Pawn::squarePosition);
  }

  public Optional<SquarePosition> nextSquareOfSecondPawnToPush() {
    return Optional.ofNullable(secondPawn)
        .flatMap(pawn -> pawn.projected(direction))
        .map(Pawn::squarePosition);
  }

  public Set<Pawn> pawnsToPush() {
    return concat(Optional.ofNullable(firstPawn).stream(), Optional.ofNullable(secondPawn).stream())
        .collect(Collectors.toSet());
  }

  public Set<Pawn> futurePawns() {
    return pawnsToPush().stream()
        .flatMap(pawn -> pawn.projected(direction).stream())
        .collect(Collectors.toSet());
  }

  public boolean canPushTwoPawns() {
    return autorizedPawnsToPush == 2;
  }
}
