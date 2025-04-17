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

        @Test
        void player_should_not_be_able_to_play_an_enemy_pawn() {
            // given
            Game game = new Game();
            SquarePosition whitePosition = new SquarePosition(6, 10, 12);
            Move invalidBlackMove = new Move(whitePosition, Direction.FORWARD_Y);

            // when then
            assertThatThrownBy(() -> game.play(Player.BLACK, invalidBlackMove))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void player_should_not_be_able_to_play_an_empty_position() {
            // given
            Game game = new Game();
            SquarePosition emptyPosition = new SquarePosition(2, 2, 8);
            Move invalidBlackMove = new Move(emptyPosition, Direction.FORWARD_Y);

            // when then
            assertThatThrownBy(() -> game.play(Player.BLACK, invalidBlackMove))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void player_should_not_be_able_to_play_an_non_existing_position() {
            // given
            Game game = new Game();
            SquarePosition nonExistingPosition = new SquarePosition(0, 0, 0);
            Move invalidBlackMove = new Move(nonExistingPosition, Direction.FORWARD_Y);

            // when then
            assertThatThrownBy(() -> game.play(Player.BLACK, invalidBlackMove))
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