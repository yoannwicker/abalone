package com.app.domain.game;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record Move(Set<Pawn> pawns, Direction direction) {

    public Move(Pawn pawn, Direction direction) {
        this(Set.of(pawn), direction);
    }

    public Set<Pawn> movePawn() {
        return pawns.stream()
                .map(pawn -> pawn.move(direction))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }
}
