package com.app.domain.game;

import java.util.Set;
import java.util.stream.Collectors;

public class PawnFixture {

    private static final Set<SquarePosition> BLACK_PAWN_MIDBOARD_POSITIONS = Set.of(
            new SquarePosition(6, 4, 6),
            new SquarePosition(8, 5, 5),
            new SquarePosition(10, 6, 4),
            new SquarePosition(5, 5, 8),
            new SquarePosition(7, 6, 7),
            new SquarePosition(9, 7, 6),
            new SquarePosition(11, 8, 5),
            new SquarePosition(6, 7, 9),
            new SquarePosition(8, 8, 8),
            new SquarePosition(10, 9, 7)
    );

    public static Set<Pawn> blackPawnMidboard() {
        return BLACK_PAWN_MIDBOARD_POSITIONS.stream()
                .map(squarePosition -> new Pawn(Player.BLACK, squarePosition))
                .collect(Collectors.toSet());
    }

    private static final Set<SquarePosition> WHITE_PAWN_MIDBOARD_POSITIONS = Set.of(
            new SquarePosition(5, 8, 11),
            new SquarePosition(7, 9, 10),
            new SquarePosition(9, 10, 9),
            new SquarePosition(11, 11, 8),
            new SquarePosition(4, 9, 13),
            new SquarePosition(6, 10, 12),
            new SquarePosition(8, 11, 11),
            new SquarePosition(10, 12, 10),
            new SquarePosition(12, 13, 9),
            new SquarePosition(7, 12, 13),
            new SquarePosition(9, 13, 12),
            new SquarePosition(12, 10, 6)
    );

    public static Set<Pawn> whitePawnMidboard() {
        return WHITE_PAWN_MIDBOARD_POSITIONS.stream()
                .map(squarePosition -> new Pawn(Player.WHITE, squarePosition))
                .collect(Collectors.toSet());
    }
}
