package com.app.domain.game;

import java.util.Set;

// TODO : Renommer en Pawns
public record Pawn(SquarePosition squarePosition) {

    private static final Set<SquarePosition> VALID_POSITIONS = Set.of(
            new SquarePosition(4, 0, 4),
            new SquarePosition(6, 1, 3),
            new SquarePosition(8, 2, 2),
            new SquarePosition(10, 3, 1),
            new SquarePosition(12, 4, 0),
            new SquarePosition(3, 1, 6),
            new SquarePosition(5, 2, 5),
            new SquarePosition(7, 3, 4),
            new SquarePosition(9, 4, 3),
            new SquarePosition(11, 5, 2),
            new SquarePosition(13, 6, 1),
            new SquarePosition(2, 2, 8),
            new SquarePosition(4, 3, 7),
            new SquarePosition(6, 4, 6),
            new SquarePosition(8, 5, 5),
            new SquarePosition(10, 6, 4),
            new SquarePosition(12, 7, 3),
            new SquarePosition(14, 8, 2),
            new SquarePosition(1, 3, 10),
            new SquarePosition(3, 4, 9),
            new SquarePosition(5, 5, 8),
            new SquarePosition(7, 6, 7),
            new SquarePosition(9, 7, 6),
            new SquarePosition(11, 8, 5),
            new SquarePosition(13, 9, 4),
            new SquarePosition(15, 10, 3),
            new SquarePosition(0, 4, 12),
            new SquarePosition(2, 5, 11),
            new SquarePosition(4, 6, 10),
            new SquarePosition(6, 7, 9),
            new SquarePosition(8, 8, 8),
            new SquarePosition(10, 9, 7),
            new SquarePosition(12, 10, 6),
            new SquarePosition(14, 11, 5),
            new SquarePosition(16, 12, 4),
            new SquarePosition(1, 6, 13),
            new SquarePosition(3, 7, 12),
            new SquarePosition(5, 8, 11),
            new SquarePosition(7, 9, 10),
            new SquarePosition(9, 10, 9),
            new SquarePosition(11, 11, 8),
            new SquarePosition(13, 12, 7),
            new SquarePosition(15, 13, 6),
            new SquarePosition(2, 8, 14),
            new SquarePosition(4, 9, 13),
            new SquarePosition(6, 10, 12),
            new SquarePosition(8, 11, 11),
            new SquarePosition(10, 12, 10),
            new SquarePosition(12, 13, 9),
            new SquarePosition(14, 14, 8),
            new SquarePosition(4, 12, 16),
            new SquarePosition(6, 13, 15),
            new SquarePosition(8, 14, 14),
            new SquarePosition(10, 15, 13),
            new SquarePosition(12, 16, 12),
            new SquarePosition(3, 10, 15),
            new SquarePosition(5, 11, 14),
            new SquarePosition(7, 12, 13),
            new SquarePosition(9, 13, 12),
            new SquarePosition(11, 14, 11),
            new SquarePosition(13, 15, 10)
    );

    public Pawn {
        if (!VALID_POSITIONS.contains(squarePosition)) {
            throw new IllegalArgumentException("Invalid square position for pawn: " + squarePosition);
        }
    }

    public Pawn move(Direction direction) {
        return new Pawn(squarePosition.update(direction));
    }
}
