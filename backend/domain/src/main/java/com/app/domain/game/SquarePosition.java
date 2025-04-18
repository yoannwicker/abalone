package com.app.domain.game;

import java.util.Set;

// TODO: renommer pour Position ?
public record SquarePosition(int x, int y, int z) {

    private static final Set<SquarePosition> startingPositionsOfBlackPawns = Set.of(
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
            new SquarePosition(6, 4, 6),
            new SquarePosition(8, 5, 5),
            new SquarePosition(10, 6, 4)
    );


    private static final Set<SquarePosition> startingPositionsOfWhitePawns = Set.of(
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
            new SquarePosition(13, 15, 10),
            new SquarePosition(6, 10, 12),
            new SquarePosition(8, 11, 11),
            new SquarePosition(10, 12, 10)
    );

    public SquarePosition moveOnX(int value) {
        return new SquarePosition(x + value, y, z);
    }

    public SquarePosition moveOnY(int value) {
        return new SquarePosition(x, y + value, z);
    }

    public SquarePosition moveOnZ(int value) {
        return new SquarePosition(x, y, z + value);
    }
}
