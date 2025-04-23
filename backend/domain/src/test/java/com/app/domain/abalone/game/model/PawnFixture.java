package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.SquarePosition.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PawnFixture {

  private static final Set<SquarePosition> BLACK_PAWN_MIDBOARD_POSITIONS =
      Set.of(C3, C4, C5, D3, D4, D5, D6, E4, E5, E6);

  private static final Set<SquarePosition> WHITE_PAWN_MIDBOARD_POSITIONS =
      Set.of(H3, H4, G2, G3, G4, G5, G6, F3, F4, F5, F6, E7);

  public static Set<Pawn> blackPawnMidboard() {
    return BLACK_PAWN_MIDBOARD_POSITIONS.stream()
        .map(squarePosition -> new Pawn(Player.BLACK, squarePosition))
        .collect(Collectors.toSet());
  }

  public static Set<Pawn> blackPawnMidboard(Pawn... pawns) {
    return Stream.concat(
            BLACK_PAWN_MIDBOARD_POSITIONS.stream()
                .map(squarePosition -> new Pawn(Player.BLACK, squarePosition)),
            Stream.of(pawns))
        .collect(Collectors.toSet());
  }

  public static Set<Pawn> whitePawnMidboard() {
    return WHITE_PAWN_MIDBOARD_POSITIONS.stream()
        .map(squarePosition -> new Pawn(Player.WHITE, squarePosition))
        .collect(Collectors.toSet());
  }

  public static Set<Pawn> whitePawnMidboard(Pawn... pawns) {
    return Stream.concat(
            WHITE_PAWN_MIDBOARD_POSITIONS.stream()
                .map(squarePosition -> new Pawn(Player.WHITE, squarePosition)),
            Stream.of(pawns))
        .collect(Collectors.toSet());
  }

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
