package com.app.domain.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameTest {

    @Nested
    class UnexpectedArgument {
        SquarePosition anyBlackPosition = new SquarePosition(6, 4, 6);
        Move anyBlackMove = new Move(anyBlackPosition, Direction.FORWARD_Y);

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
        void push_one_pawn() {
            // given
            Game game = new Game();
            SquarePosition blackPosition = new SquarePosition(6, 4, 6);
            Move move = new Move(blackPosition, Direction.FORWARD_Y);

            // when
            game.play(Player.BLACK, move);

            // then
        }
    }

    @Nested
    class MovePawnsBack {
        @Test
        void move_one_pawn() {}
    }

}