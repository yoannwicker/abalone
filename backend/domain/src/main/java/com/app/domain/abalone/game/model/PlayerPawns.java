package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.Player.BLACK;
import static com.app.domain.abalone.game.model.Player.WHITE;
import static com.app.domain.abalone.game.model.SquarePosition.A1;
import static com.app.domain.abalone.game.model.SquarePosition.A2;
import static com.app.domain.abalone.game.model.SquarePosition.A3;
import static com.app.domain.abalone.game.model.SquarePosition.A4;
import static com.app.domain.abalone.game.model.SquarePosition.A5;
import static com.app.domain.abalone.game.model.SquarePosition.B1;
import static com.app.domain.abalone.game.model.SquarePosition.B2;
import static com.app.domain.abalone.game.model.SquarePosition.B3;
import static com.app.domain.abalone.game.model.SquarePosition.B4;
import static com.app.domain.abalone.game.model.SquarePosition.B5;
import static com.app.domain.abalone.game.model.SquarePosition.B6;
import static com.app.domain.abalone.game.model.SquarePosition.C3;
import static com.app.domain.abalone.game.model.SquarePosition.C4;
import static com.app.domain.abalone.game.model.SquarePosition.C5;
import static com.app.domain.abalone.game.model.SquarePosition.G5;
import static com.app.domain.abalone.game.model.SquarePosition.G6;
import static com.app.domain.abalone.game.model.SquarePosition.G7;
import static com.app.domain.abalone.game.model.SquarePosition.H4;
import static com.app.domain.abalone.game.model.SquarePosition.H5;
import static com.app.domain.abalone.game.model.SquarePosition.H6;
import static com.app.domain.abalone.game.model.SquarePosition.H7;
import static com.app.domain.abalone.game.model.SquarePosition.H8;
import static com.app.domain.abalone.game.model.SquarePosition.H9;
import static com.app.domain.abalone.game.model.SquarePosition.I5;
import static com.app.domain.abalone.game.model.SquarePosition.I6;
import static com.app.domain.abalone.game.model.SquarePosition.I7;
import static com.app.domain.abalone.game.model.SquarePosition.I8;
import static com.app.domain.abalone.game.model.SquarePosition.I9;

import java.util.Set;
import java.util.stream.Collectors;

public record PlayerPawns(Player playerOwner, Set<Pawn> pawns) {

  private static final int MAX_PAWNS = 14;

  private static final Set<SquarePosition> BLACK_PAWN_INITIAL_POSITIONS =
      Set.of(A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, B6, C3, C4, C5);

  private static final Set<SquarePosition> WHITE_PAWN_INITIAL_POSITIONS =
      Set.of(I5, I6, I7, I8, I9, H4, H5, H6, H7, H8, H9, G5, G6, G7);

  public PlayerPawns {
    if (pawns.size() > MAX_PAWNS) {
      throw new IllegalArgumentException(
          "The number of pawns exceeds the maximum allowed of "
              + MAX_PAWNS
              + " pawns for player "
              + playerOwner);
    }
  }

  private static Set<Pawn> fromPawnsPositions(Player player, Set<SquarePosition> pawnPositions) {
    return pawnPositions.stream()
        .map(squarePosition -> new Pawn(player, squarePosition))
        .collect(Collectors.toSet());
  }

  public static PlayerPawns initalBlackPlayerPawns() {
    return new PlayerPawns(BLACK, fromPawnsPositions(BLACK, BLACK_PAWN_INITIAL_POSITIONS));
  }

  public static PlayerPawns initalWhitePlayerPawns() {
    return new PlayerPawns(WHITE, fromPawnsPositions(WHITE, WHITE_PAWN_INITIAL_POSITIONS));
  }

  public boolean contains(Pawn pawn) {
    return pawns.contains(pawn);
  }

  public boolean illegalMove(Move move) {
    return move.pawns().stream().allMatch(pawn -> pawn.player() == playerOwner)
        && move.pawns().stream().noneMatch(pawns::contains);
  }

  public boolean hasPawnAtSamePosition(Pawn pawn) {
    return pawns.stream().anyMatch(p -> p.squarePosition().equals(pawn.squarePosition()));
  }

  public void update(Set<Pawn> pawnsToUpdate, Set<Pawn> updatedPawns) {
    pawns.removeAll(pawnsToUpdate);
    pawns.addAll(updatedPawns);
  }

  public int lostPawnsCount() {
    return MAX_PAWNS - pawns.size();
  }
}
