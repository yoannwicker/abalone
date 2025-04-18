package com.app.domain.game;

public record Move(Pawn pawn, Direction direction) {

    // TODO : mettre dans une class Pawn + validation de la position
    public Pawn movePawn() {
        return pawn.move(direction);
    }
}
