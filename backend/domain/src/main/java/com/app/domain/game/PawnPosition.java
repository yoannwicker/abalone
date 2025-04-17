package com.app.domain.game;

import java.util.Set;

public record PawnPosition(int x, int y, int z) {

    private static final Set<PawnPosition> startingPositionsOfBlackPawns = Set.of(
            new PawnPosition(4, 0, 4),
            new PawnPosition(6, 1, 3),
            new PawnPosition(8, 2, 2),
            new PawnPosition(10, 3, 1),
            new PawnPosition(12, 4, 0),
            new PawnPosition(3, 1, 6),
            new PawnPosition(5, 2, 5),
            new PawnPosition(7, 3, 4),
            new PawnPosition(9, 4, 3),
            new PawnPosition(11, 5, 2),
            new PawnPosition(13, 6, 1),
            new PawnPosition(6, 4, 6),
            new PawnPosition(8, 5, 5),
            new PawnPosition(10, 6, 4)
    );


    private static final Set<PawnPosition> startingPositionsOfWhitePawns = Set.of(
            new PawnPosition(4, 12, 16),
            new PawnPosition(6, 13, 15),
            new PawnPosition(8, 14, 14),
            new PawnPosition(10, 15, 13),
            new PawnPosition(12, 16, 12),
            new PawnPosition(3, 10, 15),
            new PawnPosition(5, 11, 14),
            new PawnPosition(7, 12, 13),
            new PawnPosition(9, 13, 12),
            new PawnPosition(11, 14, 11),
            new PawnPosition(13, 15, 10),
            new PawnPosition(6, 10, 12),
            new PawnPosition(8, 11, 11),
            new PawnPosition(10, 12, 10)
    );


    private static final Set<PawnPosition> startingEmptyPositions = Set.of(
            new PawnPosition(2, 2, 8),
            new PawnPosition(4, 3, 7),
            new PawnPosition(12, 7, 3),
            new PawnPosition(14, 8, 2),
            new PawnPosition(1, 3, 10),
            new PawnPosition(3, 4, 9),
            new PawnPosition(5, 5, 8),
            new PawnPosition(7, 6, 7),
            new PawnPosition(9, 7, 6),
            new PawnPosition(11, 8, 5),
            new PawnPosition(13, 9, 4),
            new PawnPosition(15, 10, 3),
            new PawnPosition(0, 4, 12),
            new PawnPosition(2, 5, 11),
            new PawnPosition(4, 6, 10),
            new PawnPosition(6, 7, 9),
            new PawnPosition(8, 8, 8),
            new PawnPosition(10, 9, 7),
            new PawnPosition(12, 10, 6),
            new PawnPosition(14, 11, 5),
            new PawnPosition(16, 12, 4),
            new PawnPosition(1, 6, 13),
            new PawnPosition(3, 7, 12),
            new PawnPosition(5, 8, 11),
            new PawnPosition(7, 9, 10),
            new PawnPosition(9, 10, 9),
            new PawnPosition(11, 11, 8),
            new PawnPosition(13, 12, 7),
            new PawnPosition(15, 13, 6),
            new PawnPosition(2, 8, 14),
            new PawnPosition(4, 9, 13),
            new PawnPosition(12, 13, 9),
            new PawnPosition(14, 14, 8)
    );

    public static boolean isStartingPositionsOfBlackPawns(int x, int y, int z) {
        return startingPositionsOfBlackPawns.contains(new PawnPosition(x, y, z));
    }

    public static boolean isStartingPositionsOfWhitePawns(int x, int y, int z) {
        return startingPositionsOfWhitePawns.contains(new PawnPosition(x, y, z));
    }

    public static boolean isStartingEmptyPositions(int x, int y, int z) {
        return startingEmptyPositions.contains(new PawnPosition(x, y, z));
    }
}
