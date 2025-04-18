package com.app.domain.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Nested
    class PlayerTurn {
        SquarePosition anyBlackPosition = new SquarePosition(6, 4, 6);
        Pawn blackPawn = new Pawn(anyBlackPosition);
        Move anyBlackMove = new Move(blackPawn, Direction.MOVE_FORWARD_Y);

        @Test
        void white_player_should_not_be_able_to_play_first() {
            // given
            Game game = new Game();

            // when then
            assertThatThrownBy(() -> game.play(Player.WHITE, anyBlackMove))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void player_should_not_be_able_to_play_if_it_is_not_his_turn() {
            // given
            Game game = new Game();
            game.play(Player.BLACK, anyBlackMove);

            // when then
            assertThatThrownBy(() -> game.play(Player.BLACK, anyBlackMove))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class MoveOnePawn {

        @Test
        void move_forward_one_pawn_in_X() {
            // given
            Game game = new Game();
            SquarePosition blackPosition = new SquarePosition(10, 6, 4);
            Pawn pawn = new Pawn(blackPosition);

            // when
            Move move = new Move(pawn, Direction.MOVE_FORWARD_X);
            var movedPawn = game.play(Player.BLACK, move);

            // then
            var expectedPosition = new SquarePosition(12, 7, 3);
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }

        @Test
        void move_back_one_pawn_in_X() {
            // given
            Game game = new Game();

            Pawn blackPawn = new Pawn(new SquarePosition(10, 6, 4));
            game.play(Player.BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_X));

            Pawn pawn = new Pawn(new SquarePosition(6, 10, 12));

            // when
            Move move = new Move(pawn, Direction.MOVE_BACK_X);
            var movedPawn = game.play(Player.WHITE, move);

            // then
            var expectedPosition = new SquarePosition(4, 9, 13 );
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }

        @Test
        void move_forward_one_pawn_in_Y() {
            // given
            Game game = new Game();
            SquarePosition blackPosition = new SquarePosition(6, 4, 6);
            Pawn pawn = new Pawn(blackPosition);

            // when
            Move move = new Move(pawn, Direction.MOVE_FORWARD_Y);
            var movedPawn = game.play(Player.BLACK, move);

            // then
            var expectedPosition = new SquarePosition(7, 6, 7);
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }

        @Test
        void move_back_one_pawn_in_Y() {
            // given
            Game game = new Game();

            Pawn blackPawn = new Pawn(new SquarePosition(6, 4, 6));
            game.play(Player.BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_Y));

            Pawn pawn = new Pawn(new SquarePosition(10, 12, 10));

            // when
            Move move = new Move(pawn, Direction.MOVE_BACK_Y);
            var movedPawn = game.play(Player.WHITE, move);

            // then
            var expectedPosition = new SquarePosition(9, 10, 9 );
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }

        @Test
        void move_forward_one_pawn_in_Z() {
            // given
            Game game = new Game();
            SquarePosition blackPosition = new SquarePosition(6, 4, 6);
            Pawn pawn = new Pawn(blackPosition);

            // when
            Move move = new Move(pawn, Direction.MOVE_FORWARD_Z);
            var movedPawn = game.play(Player.BLACK, move);

            // then
            var expectedPosition = new SquarePosition(5, 5, 8);
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }

        @Test
        void move_back_one_pawn_in_Z() {
            // given
            Game game = new Game();

            Pawn blackPawn = new Pawn(new SquarePosition(6, 4, 6));
            game.play(Player.BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_Z));

            Pawn pawn = new Pawn(new SquarePosition(10, 12, 10));

            // when
            Move move = new Move(pawn, Direction.MOVE_BACK_Z);
            var movedPawn = game.play(Player.WHITE, move);

            // then
            var expectedPosition = new SquarePosition(11, 11, 8);
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }

        @Test
        void should_not_be_able_to_move_a_non_existent_black_pawn() {
            // given
            Game game = new Game();
            SquarePosition blackPosition = new SquarePosition(12, 7, 3);
            Pawn pawn = new Pawn(blackPosition);

            // when then
            Move move = new Move(pawn, Direction.MOVE_FORWARD_X);
            assertThatThrownBy(() -> game.play(Player.BLACK, move))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void should_not_be_able_to_move_a_non_existent_white_pawn() {
            // given
            Game game = new Game();

            Pawn blackPawn = new Pawn(new SquarePosition(10, 6, 4));
            game.play(Player.BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_X));

            Pawn pawn = new Pawn(new SquarePosition(4, 9, 13 ));

            // when then
            Move move = new Move(pawn, Direction.MOVE_BACK_X);
            assertThatThrownBy(() -> game.play(Player.WHITE, move))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void should_be_able_to_move_the_same_pawn() {
            // given
            Game game = new Game();

            SquarePosition initialBlackPawnPosition = new SquarePosition(10, 6, 4);
            var movedBlackPawn = game.play(Player.BLACK, new Move(new Pawn(initialBlackPawnPosition), Direction.MOVE_FORWARD_X));
            SquarePosition initialWhitePawnPosition = new SquarePosition(6, 10, 12);
            var movedWhitePawn = game.play(Player.WHITE, new Move(new Pawn(initialWhitePawnPosition), Direction.MOVE_BACK_X));

            // when then
            assertThatCode(() -> {
                game.play(Player.BLACK, new Move(movedBlackPawn, Direction.MOVE_FORWARD_X));
                game.play(Player.WHITE, new Move(movedWhitePawn, Direction.MOVE_BACK_X));
            }).doesNotThrowAnyException();
        }
    }

}