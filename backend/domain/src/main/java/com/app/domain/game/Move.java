package com.app.domain.game;

public record Move(Pawn pawn, Direction direction) {

    public Pawn movePawn() {
        return pawn.move(direction);
    }
}
