package com.app.domain.game;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameTest {

    @Test
    void white_player_should_not_be_able_to_play_first() {
        // given
        Game game = new Game();

        // when then
        assertThatThrownBy(() -> game.play(Player.WHITE, new Move()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void player_should_not_be_able_to_play_if_it_is_not_his_turn() {
        // given
        Game game = new Game();
        game.play(Player.BLACK, new Move());

        // when then
        assertThatThrownBy(() -> game.play(Player.BLACK, new Move()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}