package com.app.domain.game;

import java.util.Set;

public record Move(Set<Pawn> pawns, Direction direction) {

    public Move(Pawn pawn, Direction direction) {
        this(Set.of(pawn), direction);
    }

    public Set<Pawn> movePawn() {
        return pawns.stream().findAny().orElseThrow().move(direction);
    }
}
