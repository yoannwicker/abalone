package com.app.domain.abalone.game.model;

import java.util.Set;

public record PlayerPawns(Player playerOwner, Set<Pawn> pawns) {

  private static final int MAX_PAWNS = 14;

  public PlayerPawns {
    if (pawns.size() > MAX_PAWNS) {
      throw new IllegalArgumentException(
          "The number of pawns exceeds the maximum allowed of "
              + MAX_PAWNS
              + " pawns for player "
              + playerOwner);
    }
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
