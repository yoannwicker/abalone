package com.app.domain.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class GameBoardTest {

    @Nested
    class StartingPosition {

        @ParameterizedTest
        @CsvSource({
                "   0  ,   0   ,    0   ",
                "   1  ,   0   ,    0   ",
                "   2  ,   0   ,    0   ",
                "   0  ,   1   ,    0   ",
                "   0  ,   2   ,    0   ",
                "   0  ,   0   ,    1   ",
                "   0  ,   0   ,    2   ",
                "   16 ,   16  ,    16  ",
                "   15 ,   16  ,    16  ",
                "   14 ,   16  ,    16  ",
        })
        void should_be_a_non_existent_location(int x, int y, int z) {
            var gameBoard = new GameBoard();

            assertThat(gameBoard.getSquare(x, y, z)).isNull();
        }

        @ParameterizedTest
        @CsvSource({
                "   4  ,   0   ,    4   ",
                "   6  ,   1   ,    3   ",
                "   8  ,   2   ,    2   ",
                "   10 ,   3   ,    1   ",
                "   12 ,   4   ,    0   ",
                "   3  ,   1   ,    6   ",
                "   5  ,   2   ,    5   ",
                "   7  ,   3   ,    4   ",
                "   9  ,   4   ,    3   ",
                "   11 ,   5   ,    2   ",
                "   13 ,   6   ,    1   ",
                "   6  ,   4   ,    6   ",
                "   8  ,   5   ,    5   ",
                "   10 ,   6   ,    4   "
        })
        void should_be_a_black_pawn_starting_location(int x, int y, int z) {
            var gameBoard = new GameBoard();

            assertThat(gameBoard.getSquare(x, y, z)).isEqualTo(Square.BLACK_PAWN);
        }

        @ParameterizedTest
        @CsvSource({
                "   4  ,   12  ,    16  ",
                "   6  ,   13  ,    15  ",
                "   8  ,   14  ,    14  ",
                "   10 ,   15  ,    13  ",
                "   12 ,   16  ,    12  ",
                "   3  ,   10  ,    15  ",
                "   5  ,   11  ,    14  ",
                "   7  ,   12  ,    13  ",
                "   9  ,   13  ,    12  ",
                "   11 ,   14  ,    11  ",
                "   13 ,   15  ,    10  ",
                "   6  ,   10  ,    12  ",
                "   8  ,   11  ,    11  ",
                "   10 ,   12  ,    10  "
        })
        void should_be_a_white_pawn_starting_location(int x, int y, int z) {
            var gameBoard = new GameBoard();

            assertThat(gameBoard.getSquare(x, y, z)).isEqualTo(Square.WHITE_PAWN);
        }

        @ParameterizedTest
        @CsvSource({
                "   2  ,   2   ,    8   ",
                "   4  ,   3   ,    7   ",
                "   12 ,   7   ,    3   ",
                "   14 ,   8   ,    2   ",
                "   1  ,   3   ,    10  ",
                "   3  ,   4   ,    9   ",
                "   5  ,   5   ,    8   ",
                "   7  ,   6   ,    7   ",
                "   9  ,   7   ,    6   ",
                "   11 ,   8   ,    5   ",
                "   13 ,   9   ,    4   ",
                "   15 ,   10  ,    3   ",
                "   0  ,   4   ,    12  ",
                "   2  ,   5   ,    11  ",
                "   4  ,   6   ,    10  ",
                "   6  ,   7   ,    9   ",
                "   8  ,   8   ,    8   ",
                "   10 ,   9   ,    7   ",
                "   12 ,   10  ,    6   ",
                "   14 ,   11  ,    5   ",
                "   16 ,   12  ,    4   ",
                "   1  ,   6   ,    13  ",
                "   3  ,   7   ,    12  ",
                "   5  ,   8   ,    11  ",
                "   7  ,   9   ,    10  ",
                "   9  ,   10  ,    9   ",
                "   11 ,   11  ,    8   ",
                "   13 ,   12  ,    7   ",
                "   15 ,   13  ,    6   ",
                "   2  ,   8   ,    14  ",
                "   4  ,   9   ,    13  ",
                "   12 ,   13  ,    9   ",
                "   14 ,   14  ,    8   "
        })
        void should_be_an_empty_starting_location(int x, int y, int z) {
            var gameBoard = new GameBoard();

            assertThat(gameBoard.getSquare(x, y, z)).isEqualTo(Square.EMPTY);
        }

        @Test
        void should_have_61_squares() {
            var gameBoard = new GameBoard();
            List<Square> squares = IntStream.range(0, 17)
                    .boxed()
                    .flatMap(x -> IntStream.range(0, 17).boxed()
                            .flatMap(y -> IntStream.range(0, 17).boxed()
                                    .map(z -> gameBoard.getSquare(x, y, z))
                                    .filter(Objects::nonNull)
                            )
                    )
                    .toList();

            assertThat(squares).hasSize(61);
        }
    }
}