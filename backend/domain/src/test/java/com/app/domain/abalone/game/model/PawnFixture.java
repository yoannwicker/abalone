package com.app.domain.abalone.game.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PawnFixture {

  public static Set<Pawn> whitePawns(SquarePosition... positions) {
    return Arrays.stream(positions)
        .map(squarePosition -> new Pawn(Player.WHITE, squarePosition))
        .collect(Collectors.toSet());
  }

  public static Set<Pawn> blackPawns(SquarePosition... positions) {
    return Arrays.stream(positions)
        .map(squarePosition -> new Pawn(Player.BLACK, squarePosition))
        .collect(Collectors.toSet());
  }
}
