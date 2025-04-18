package com.app.domain.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameTest {

    @Nested
    class UnexpectedArgument {
        SquarePosition anyBlackPosition = new SquarePosition(6, 4, 6);
        Pawn blackPawn = new Pawn(anyBlackPosition);
        Move anyBlackMove = new Move(blackPawn, Direction.FORWARD_Y);

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
    class PushingPawns {
        @Test
        void move_forward_one_pawn_in_Y() {
            // given
            Game game = new Game();
            SquarePosition blackPosition = new SquarePosition(6, 4, 6);
            Pawn pawn = new Pawn(blackPosition);
            Move move = new Move(pawn, Direction.FORWARD_Y);

            // when
            var movedPawn = game.play(Player.BLACK, move);

            // then
            var expectedPosition = new SquarePosition(7, 6, 7);
            var expectedPawn = new Pawn(expectedPosition);
            assertThat(movedPawn).isEqualTo(expectedPawn);
        }
    }

    @Nested
    class MovePawnsBack {
        @Test
        void move_one_pawn() {}
    }

}