package com.app.domain.game;

import java.util.Optional;

public record Move(Pawn pawn, Direction direction) {

    public Optional<Pawn> movePawn() {
        return pawn.move(direction);
    }
}
