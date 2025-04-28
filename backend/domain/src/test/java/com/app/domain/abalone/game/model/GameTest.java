package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.PawnFixture.blackPawnMidboard;
import static com.app.domain.abalone.game.model.PawnFixture.blackPawns;
import static com.app.domain.abalone.game.model.PawnFixture.whitePawnMidboard;
import static com.app.domain.abalone.game.model.PawnFixture.whitePawns;
import static com.app.domain.abalone.game.model.Player.BLACK;
import static com.app.domain.abalone.game.model.Player.WHITE;
import static com.app.domain.abalone.game.model.SquarePosition.*;
import static org.assertj.core.api.Assertions.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GameTest {

  private static void assertThatNoPawnLost(Game game) {
    assertThatPawnLost(game.blackPlayerPawns, 0);
    assertThatPawnLost(game.whitePlayerPawns, 0);
  }

  private static void assertThatNoPawnLost(PlayResult playResult) {
    assertThat(playResult)
        .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
        .containsExactly(0, 0, GameStatus.inProgress());
  }

  private static void assertThatPawnLost(PlayerPawns game, int expected) {
    assertThat(game).extracting(PlayerPawns::lostPawnsCount).isEqualTo(expected);
  }

  @Nested
  class PlayerTurn {
    Pawn blackPawn = new Pawn(BLACK, C3);
    Move anyBlackMove = new Move(blackPawn, Direction.MOVE_FORWARD_Y);

    @Test
    void white_player_should_not_be_able_to_play_first() {
      // given
      Game game = new Game();

      // when then
      assertThatThrownBy(() -> game.play(WHITE, anyBlackMove))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void player_should_not_be_able_to_play_if_it_is_not_his_turn() {
      // given
      Game game = new Game();
      game.play(BLACK, anyBlackMove);

      // when then
      assertThatThrownBy(() -> game.play(BLACK, anyBlackMove))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  class GameStatusTest {

    @Test
    void should_be_able_to_play() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));

      Pawn blackPawn = new Pawn(BLACK, B3);
      Move validBlackMove = new Move(blackPawn, Direction.MOVE_FORWARD_Y);

      // when then
      assertThatCode(() -> game.play(BLACK, validBlackMove)).doesNotThrowAnyException();
    }

    @Test
    void should_not_be_able_to_play_if_player_already_lost() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));

      Pawn blackPawn = new Pawn(BLACK, B3);
      Move validBlackMove = new Move(blackPawn, Direction.MOVE_FORWARD_Y);

      // when then
      assertThatThrownBy(() -> game.play(BLACK, validBlackMove))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_not_be_able_to_play_if_player_already_win() {
      // given
      Game game =
          new Game(
              WHITE,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));

      Pawn whitePawn = new Pawn(WHITE, H3);
      Move validWhiteMove = new Move(whitePawn, Direction.MOVE_BACK_Y);

      // when then
      assertThatThrownBy(() -> game.play(WHITE, validWhiteMove))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_not_be_able_to_play_if_game_finished_by_draw() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      game.drawMatch();

      Pawn blackPawn = new Pawn(BLACK, B3);
      Move validBlackMove = new Move(blackPawn, Direction.MOVE_FORWARD_Y);

      // when then
      assertThatThrownBy(() -> game.play(BLACK, validBlackMove))
          .isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  class DrawMatch {

    @Test
    void should_be_able_to_draw_match() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));

      // when
      var playResult = game.drawMatch();

      // then
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(5, 5, GameStatus.draw());
    }

    @Test
    void should_not_be_able_to_draw_match_if_game_already_finished() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3));

      // when then
      assertThatThrownBy(game::drawMatch).isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  class MoveOnePawn {

    @Test
    void move_forward_one_pawn_in_X() {
      // given
      Game game = new Game();
      Pawn pawn = new Pawn(BLACK, C5);

      // when
      Move move = new Move(pawn, Direction.MOVE_FORWARD_X);
      var playResult = game.play(BLACK, move);

      // then
      var expectedPawn = new Pawn(BLACK, C6);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_back_one_pawn_in_X() {
      // given
      Game game = new Game();

      Pawn pawn = new Pawn(BLACK, C3);

      // when
      Move move = new Move(pawn, Direction.MOVE_BACK_X);
      var playResult = game.play(BLACK, move);

      // then
      var expectedPawn = new Pawn(BLACK, C2);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_forward_one_pawn_in_Y() {
      // given
      Game game = new Game();

      Pawn pawn = new Pawn(BLACK, C3);

      // when
      Move move = new Move(pawn, Direction.MOVE_FORWARD_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedPawn = new Pawn(BLACK, D4);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_back_one_pawn_in_Y() {
      // given
      Game game = new Game();

      Pawn blackPawn = new Pawn(BLACK, C3);
      game.play(BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_Y));

      Pawn pawn = new Pawn(WHITE, G5);

      // when
      Move move = new Move(pawn, Direction.MOVE_BACK_Y);
      var playResult = game.play(WHITE, move);

      // then
      var expectedPawn = new Pawn(WHITE, F5);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_forward_one_pawn_in_Z() {
      // given
      Game game = new Game();
      Pawn pawn = new Pawn(BLACK, C3);

      // when
      Move move = new Move(pawn, Direction.MOVE_FORWARD_Z);
      var playResult = game.play(BLACK, move);

      // then
      var expectedPawn = new Pawn(BLACK, D3);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_back_one_pawn_in_Z() {
      // given
      Game game = new Game();

      Pawn blackPawn = new Pawn(BLACK, C3);
      game.play(BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_Z));

      Pawn pawn = new Pawn(WHITE, G5);

      // when
      Move move = new Move(pawn, Direction.MOVE_BACK_Z);
      var playResult = game.play(WHITE, move);

      // then
      var expectedPawn = new Pawn(WHITE, F6);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void should_not_be_able_to_move_a_non_existent_black_pawn() {
      // given
      Game game = new Game();
      Pawn pawn = new Pawn(BLACK, C6);

      // when then
      Move move = new Move(pawn, Direction.MOVE_FORWARD_X);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalArgumentException.class);
      assertThatNoPawnLost(game);
    }

    @Test
    void should_not_be_able_to_move_a_non_existent_white_pawn() {
      // given
      Game game = new Game();

      Pawn blackPawn = new Pawn(BLACK, C5);
      game.play(BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_X));

      Pawn pawn = new Pawn(WHITE, G2);

      // when then
      Move move = new Move(pawn, Direction.MOVE_BACK_X);
      assertThatThrownBy(() -> game.play(WHITE, move)).isInstanceOf(IllegalArgumentException.class);
      assertThatNoPawnLost(game);
    }

    @Test
    void should_be_able_to_move_the_same_pawn() {
      // given
      Game game = new Game();

      var blackPlayResult =
          game.play(BLACK, new Move(new Pawn(BLACK, C5), Direction.MOVE_FORWARD_X));
      var whitePlayResult = game.play(WHITE, new Move(new Pawn(WHITE, G3), Direction.MOVE_BACK_X));

      // when then
      assertThatCode(
              () -> {
                game.play(BLACK, new Move(blackPlayResult.movedPawns(), Direction.MOVE_FORWARD_X));
                game.play(WHITE, new Move(whitePlayResult.movedPawns(), Direction.MOVE_BACK_X));
              })
          .doesNotThrowAnyException();
      assertThatNoPawnLost(game);
    }

    @Test
    void should_not_be_able_to_move_a_black_pawn_when_there_is_already_a_black_pawn_on_it() {
      // given
      Game game = new Game();
      Pawn pawn = new Pawn(BLACK, C5);

      // when then
      Move move = new Move(pawn, Direction.MOVE_BACK_X);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatNoPawnLost(game);
    }

    @Test
    void should_not_be_able_to_move_a_white_pawn_when_there_is_already_a_white_pawn_on_it() {
      // given
      Game game = new Game();

      Pawn blackPawn = new Pawn(BLACK, C5);
      game.play(BLACK, new Move(blackPawn, Direction.MOVE_FORWARD_X));

      Pawn pawn = new Pawn(WHITE, G3);

      // when
      Move move = new Move(pawn, Direction.MOVE_FORWARD_X);
      assertThatThrownBy(() -> game.play(WHITE, move)).isInstanceOf(IllegalStateException.class);
      assertThatNoPawnLost(game);
    }

    @Test
    void should_not_be_able_to_move_a_black_pawn_when_there_is_already_a_white_pawn_on_it() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
      Pawn pawn = new Pawn(BLACK, E5);

      // when then
      Move move = new Move(pawn, Direction.MOVE_FORWARD_X);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
    }

    @Test
    void should_not_be_able_to_move_a_white_pawn_when_there_is_already_a_black_pawn_on_it() {
      // given
      Game game = new Game(WHITE, blackPawnMidboard(), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
      Pawn pawn = new Pawn(WHITE, F4);

      // when then
      Move move = new Move(pawn, Direction.MOVE_BACK_X);
      assertThatThrownBy(() -> game.play(WHITE, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
    }

    @Test
    void should_lose_pawn() {
      // given
      Game game = new Game();
      assertThatNoPawnLost(game);
      Pawn pawn = new Pawn(BLACK, A1);

      // when
      Move move = new Move(pawn, Direction.MOVE_BACK_X);
      var playResult = game.play(BLACK, move);

      // then
      assertThat(playResult.movedPawns()).isEmpty();
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(1, 0);
    }

    @Test
    void player_black_should_lose_pawn_and_lose_the_game() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 5);

      Pawn pawn = new Pawn(BLACK, A1);

      // when
      Move move = new Move(pawn, Direction.MOVE_BACK_X);
      var playResult = game.play(BLACK, move);

      // then
      assertThat(playResult.movedPawns()).isEmpty();
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(6, 5, new GameStatus(true, Optional.of(WHITE)));
    }

    @Test
    void player_white_should_lose_pawn_and_lose_the_game() {
      // given
      Game game =
          new Game(
              WHITE,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 5);

      Pawn pawn = new Pawn(WHITE, I1);

      // when
      Move move = new Move(pawn, Direction.MOVE_FORWARD_Z);
      var playResult = game.play(WHITE, move);

      // then
      assertThat(playResult.movedPawns()).isEmpty();
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(5, 6, new GameStatus(true, Optional.of(BLACK)));
    }
  }

  @Nested
  class MoveTwoPawnsOnALine {

    @Test
    void move_two_pawns() {
      // given
      Game game = new Game();
      Pawn firstPawn = new Pawn(BLACK, B2);
      Pawn secondPawn = new Pawn(BLACK, C3);

      // when
      LinkedHashSet<Pawn> pawnsToMove = new LinkedHashSet<>(List.of(firstPawn, secondPawn));
      Move move = new Move(pawnsToMove, Direction.MOVE_FORWARD_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, C3);
      var expectedSecondPawn = new Pawn(BLACK, D4);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_two_pawns_and_lose_one_pawn() {
      // given
      Game game = new Game();
      assertThatNoPawnLost(game);

      Pawn firstPawn = new Pawn(BLACK, A1);
      Pawn secondPawn = new Pawn(BLACK, B2);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedPawn = new Pawn(BLACK, A1);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(1, 0);
    }

    @Test
    void player_black_lose_one_pawn_and_lose_the_game() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, G2, F3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, C2, D3));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 5);

      Pawn firstPawn = new Pawn(BLACK, A1);
      Pawn secondPawn = new Pawn(BLACK, B2);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedPawn = new Pawn(BLACK, A1);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(6, 5, new GameStatus(true, Optional.of(WHITE)));
    }

    @Test
    void player_white_lose_one_pawn_and_lose_the_game() {
      // given
      Game game =
          new Game(
              WHITE,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, G2, F3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, C2, D3));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 5);

      Pawn firstPawn = new Pawn(WHITE, I1);
      Pawn secondPawn = new Pawn(WHITE, I2);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_X);
      var playResult = game.play(WHITE, move);

      // then
      var expectedPawn = new Pawn(WHITE, I1);
      assertThat(playResult.movedPawns()).containsExactly(expectedPawn);
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(5, 6, new GameStatus(true, Optional.of(BLACK)));
    }

    @Test
    void should_not_be_able_to_move_two_black_pawns_when_there_is_already_a_black_pawn_on_it() {
      // given
      Game game = new Game();
      Pawn firstPawn = new Pawn(BLACK, A1);
      Pawn secondPawn = new Pawn(BLACK, B2);

      // when then
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Y);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatNoPawnLost(game);
    }

    @Test
    void move_two_black_pawns_and_one_white_pawn() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
      Pawn firstPawn = new Pawn(BLACK, C5);
      Pawn secondPawn = new Pawn(BLACK, D6);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Y);
      var playResult = game.play(BLACK, move);

      // then
      Pawn expectedFirstPawn = new Pawn(BLACK, D6);
      Pawn expectedSecondPawn = new Pawn(BLACK, E7);
      Pawn expectedMovedWhitePawn = new Pawn(WHITE, F7);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn, expectedMovedWhitePawn);
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(4, 2);
    }

    @Test
    void player_black_move_two_pawns_to_remove_one_white_pawn_and_win_the_game() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, G2, F3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, C2, D3));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 5);

      Pawn firstPawn = new Pawn(BLACK, G2);
      Pawn secondPawn = new Pawn(BLACK, F3);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Z);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, H1);
      var expectedSecondPawn = new Pawn(BLACK, G2);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(5, 6, new GameStatus(true, Optional.of(BLACK)));
    }

    @Test
    void player_white_move_two_pawns_to_remove_one_black_pawn_and_win_the_game() {
      // given
      Game game =
          new Game(
              WHITE,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, G2, F3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, C2, D3));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 5);

      Pawn firstPawn = new Pawn(WHITE, C2);
      Pawn secondPawn = new Pawn(WHITE, D3);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_Y);
      var playResult = game.play(WHITE, move);

      // then
      var expectedFirstPawn = new Pawn(WHITE, B1);
      var expectedSecondPawn = new Pawn(WHITE, C2);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
      assertThat(playResult)
          .extracting(
              PlayResult::blackPawnsLost, PlayResult::whitePawnsLost, PlayResult::gameStatus)
          .containsExactly(6, 5, new GameStatus(true, Optional.of(WHITE)));
    }

    @Test
    void move_two_white_pawns_and_one_black_pawn() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard());

      Pawn firstBlackPawn = new Pawn(BLACK, C5);
      Pawn secondBlackPawn = new Pawn(BLACK, D6);
      Move blackMove = new Move(Set.of(firstBlackPawn, secondBlackPawn), Direction.MOVE_FORWARD_Y);
      game.play(BLACK, blackMove);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);

      Pawn firstWhitePawn = new Pawn(WHITE, F6);
      Pawn secondWhitePawn = new Pawn(WHITE, G5);

      // when
      Move move = new Move(Set.of(firstWhitePawn, secondWhitePawn), Direction.MOVE_BACK_Z);
      var playResult = game.play(WHITE, move);

      // then
      Pawn expectedFirstPawn = new Pawn(WHITE, F6);
      Pawn expectedSecondPawn = new Pawn(WHITE, E7);
      Pawn expectedMovedBlackPawn = new Pawn(BLACK, D7);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn, expectedMovedBlackPawn);
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(4, 2);
    }

    @Test
    void should_not_be_able_to_move_two_black_pawns_when_there_is_two_white_pawns_in_front() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);

      Pawn firstPawn = new Pawn(BLACK, D5);
      Pawn secondPawn = new Pawn(BLACK, E6);

      // when then
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Y);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
    }

    @Test
    void
        should_not_be_able_to_move_two_black_pawns_when_there_is_one_white_pawn_one_black_pawn_in_front() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(new Pawn(BLACK, F7)), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 3);
      assertThatPawnLost(game.whitePlayerPawns, 2);

      Pawn firstPawn = new Pawn(BLACK, C5);
      Pawn secondPawn = new Pawn(BLACK, D6);

      // when then
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Y);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 3);
      assertThatPawnLost(game.whitePlayerPawns, 2);
    }
  }

  @Nested
  class MoveThreePawnsOnALine {

    @Test
    void move_three_black_pawns() {
      // given
      Game game = new Game();
      Pawn firstPawn = new Pawn(BLACK, A1);
      Pawn secondPawn = new Pawn(BLACK, B2);
      Pawn thirdPawn = new Pawn(BLACK, C3);

      // when
      Set<Pawn> pawnsToMove = Set.of(firstPawn, secondPawn, thirdPawn);
      Move move = new Move(pawnsToMove, Direction.MOVE_FORWARD_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, B2);
      var expectedSecondPawn = new Pawn(BLACK, C3);
      var expectedThirdPawn = new Pawn(BLACK, D4);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn, expectedThirdPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_three_white_pawns() {
      // given
      Game game = new Game();

      game.play(BLACK, new Move(Set.of(new Pawn(BLACK, C3)), Direction.MOVE_FORWARD_Y));

      Pawn firstPawn = new Pawn(WHITE, I5);
      Pawn secondPawn = new Pawn(WHITE, H5);
      Pawn thirdPawn = new Pawn(WHITE, G5);

      // when
      Set<Pawn> pawnsToMove = Set.of(firstPawn, secondPawn, thirdPawn);
      Move move = new Move(pawnsToMove, Direction.MOVE_BACK_Y);
      var playResult = game.play(WHITE, move);

      // then
      var expectedFirstPawn = new Pawn(WHITE, F5);
      var expectedSecondPawn = new Pawn(WHITE, G5);
      var expectedThirdPawn = new Pawn(WHITE, H5);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn, expectedThirdPawn);
      assertThatNoPawnLost(playResult);
    }

    @Test
    void move_three_white_pawns_in_the_middle() {
      // given
      Game game =
          new Game(
              WHITE,
              blackPawns(C3, C4, C5, D3, D4, D5, E4, E5, C2),
              whitePawns(H3, H4, G2, G3, G4, G5, G6, F3, F4, F5, E6, D6));

      Pawn firstPawn = new Pawn(WHITE, F5);
      Pawn secondPawn = new Pawn(WHITE, E6);
      Pawn thirdPawn = new Pawn(WHITE, D6);

      // when
      Set<Pawn> pawnsToMove = Set.of(firstPawn, secondPawn, thirdPawn);
      Move move = new Move(pawnsToMove, Direction.MOVE_BACK_Z);
      var playResult = game.play(WHITE, move);

      // then
      var expectedFirstPawn = new Pawn(WHITE, E6);
      var expectedSecondPawn = new Pawn(WHITE, D6);
      var expectedThirdPawn = new Pawn(WHITE, C6);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn, expectedThirdPawn);
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(5, 2);
    }

    @Test
    void move_three_black_pawns_and_two_white_pawns() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);

      Pawn firstPawn = new Pawn(BLACK, C4);
      Pawn secondPawn = new Pawn(BLACK, D5);
      Pawn thirdPawn = new Pawn(BLACK, E6);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y);
      var playResult = game.play(BLACK, move);

      // then
      Pawn expectedFirstPawn = new Pawn(BLACK, D5);
      Pawn expectedSecondPawn = new Pawn(BLACK, E6);
      Pawn expectedThirdPawn = new Pawn(BLACK, F6);
      Pawn expectedFirstWhitePawn = new Pawn(WHITE, G6);
      Pawn expectedSecondWhitePawn = new Pawn(WHITE, H6);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(
              expectedFirstPawn,
              expectedSecondPawn,
              expectedThirdPawn,
              expectedFirstWhitePawn,
              expectedSecondWhitePawn);
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(4, 2);
    }

    @Test
    void move_three_white_pawns_and_two_black_pawns() {
      // given
      Game game = new Game(WHITE, blackPawnMidboard(), whitePawnMidboard());
      Pawn firstPawn = new Pawn(WHITE, F5);
      Pawn secondPawn = new Pawn(WHITE, G4);
      Pawn thirdPawn = new Pawn(WHITE, H3);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_BACK_Z);
      var playResult = game.play(WHITE, move);

      // then
      Pawn expectedFirstPawn = new Pawn(WHITE, E6);
      Pawn expectedSecondPawn = new Pawn(WHITE, F5);
      Pawn expectedThirdPawn = new Pawn(WHITE, G4);
      Pawn expectedFirstBlackPawn = new Pawn(BLACK, D6);
      Pawn expectedSecondBlackPawn = new Pawn(BLACK, C6);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(
              expectedFirstPawn,
              expectedSecondPawn,
              expectedThirdPawn,
              expectedFirstBlackPawn,
              expectedSecondBlackPawn);
      assertThat(playResult)
          .extracting(PlayResult::blackPawnsLost, PlayResult::whitePawnsLost)
          .containsExactly(4, 2);
    }

    @Test
    void should_not_be_able_to_move_three_black_pawns_when_there_is_three_white_pawns_in_front() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard(new Pawn(WHITE, H6)));
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 1);

      Pawn firstPawn = new Pawn(BLACK, C4);
      Pawn secondPawn = new Pawn(BLACK, D5);
      Pawn thirdPawn = new Pawn(BLACK, E6);

      // when then
      Move move = new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 1);
    }

    @Test
    void
        should_not_be_able_to_move_three_black_pawns_when_there_is_two_white_pawns_one_black_pawn_in_front() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(new Pawn(BLACK, H6)), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 3);
      assertThatPawnLost(game.whitePlayerPawns, 2);

      Pawn firstPawn = new Pawn(BLACK, C4);
      Pawn secondPawn = new Pawn(BLACK, D5);
      Pawn thirdPawn = new Pawn(BLACK, E6);

      // when then
      Move move = new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y);
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 3);
      assertThatPawnLost(game.whitePlayerPawns, 2);
    }
  }

  @Nested
  class MoveTwoPawnsToSide {

    @Test
    void move_two_pawns_forward_x() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, D3, E3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      Pawn firstPawn = new Pawn(BLACK, D3);
      Pawn secondPawn = new Pawn(BLACK, E3);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_X);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, D4);
      var expectedSecondPawn = new Pawn(BLACK, E4);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
    }

    @Test
    void move_two_pawns_back_x() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, D3, E3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      Pawn firstPawn = new Pawn(BLACK, D3);
      Pawn secondPawn = new Pawn(BLACK, E3);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_X);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, D2);
      var expectedSecondPawn = new Pawn(BLACK, E2);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
    }

    @Test
    void move_two_pawns_forward_y() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, D3, E3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      Pawn firstPawn = new Pawn(BLACK, D3);
      Pawn secondPawn = new Pawn(BLACK, E3);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, E4);
      var expectedSecondPawn = new Pawn(BLACK, F3);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
    }

    @Test
    void move_two_pawns_back_y() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, D3, E3),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      Pawn firstPawn = new Pawn(BLACK, D3);
      Pawn secondPawn = new Pawn(BLACK, E3);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_Y);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, C2);
      var expectedSecondPawn = new Pawn(BLACK, D2);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
    }

    @Test
    void move_two_pawns_forward_z() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, D3, D4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      Pawn firstPawn = new Pawn(BLACK, D3);
      Pawn secondPawn = new Pawn(BLACK, D4);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Z);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, E3);
      var expectedSecondPawn = new Pawn(BLACK, E4);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
    }

    @Test
    void move_two_pawns_back_z() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, D3, D4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));
      Pawn firstPawn = new Pawn(BLACK, D3);
      Pawn secondPawn = new Pawn(BLACK, D4);

      // when
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_BACK_Z);
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, C3);
      var expectedSecondPawn = new Pawn(BLACK, C4);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn);
    }

    @Test
    void should_not_be_able_to_move_two_black_pawns_to_side_and_one_white_pawn() {
      // given
      Game game = new Game(BLACK, blackPawnMidboard(), whitePawnMidboard());
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
      Pawn firstPawn = new Pawn(BLACK, C6);
      Pawn secondPawn = new Pawn(BLACK, D6);
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Y);

      // when then
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 4);
      assertThatPawnLost(game.whitePlayerPawns, 2);
    }

    @Test
    void should_not_be_able_to_move_two_black_pawns_to_side_and_one_black_pawn() {
      // given
      Game game = new Game();
      Pawn firstPawn = new Pawn(BLACK, A1);
      Pawn secondPawn = new Pawn(BLACK, B2);
      Move move = new Move(Set.of(firstPawn, secondPawn), Direction.MOVE_FORWARD_Z);

      // when then
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  class MoveThreePawnsToSide {

    @Test
    void move_three_pawns() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4));

      Pawn firstPawn = new Pawn(BLACK, B1);
      Pawn secondPawn = new Pawn(BLACK, B2);
      Pawn thirdPawn = new Pawn(BLACK, B3);
      Set<Pawn> pawnsToMove = Set.of(firstPawn, secondPawn, thirdPawn);
      Move move = new Move(pawnsToMove, Direction.MOVE_FORWARD_Z);

      // when
      var playResult = game.play(BLACK, move);

      // then
      var expectedFirstPawn = new Pawn(BLACK, C1);
      var expectedSecondPawn = new Pawn(BLACK, C2);
      var expectedThirdPawn = new Pawn(BLACK, C3);
      assertThat(playResult.movedPawns())
          .containsExactlyInAnyOrder(expectedFirstPawn, expectedSecondPawn, expectedThirdPawn);
    }

    @Test
    void should_not_be_able_to_move_three_black_pawns_to_side_and_one_white_pawn() {
      // given
      Game game =
          new Game(
              BLACK,
              blackPawns(A1, A2, A3, A4, A5, B1, B2, B3, B4),
              whitePawns(I1, I2, I3, I4, I5, H1, H2, H3, H4, C2));
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 4);
      Pawn firstPawn = new Pawn(BLACK, B1);
      Pawn secondPawn = new Pawn(BLACK, B2);
      Pawn thirdPawn = new Pawn(BLACK, B3);
      Move move = new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y);

      // when then
      assertThatThrownBy(() -> game.play(BLACK, move)).isInstanceOf(IllegalStateException.class);
      assertThatPawnLost(game.blackPlayerPawns, 5);
      assertThatPawnLost(game.whitePlayerPawns, 4);
    }
  }
}
