package com.app.domain.game;

import java.util.Set;

public record Pawns(Player playerOwner, Set<Pawn> pawns) {

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
}
