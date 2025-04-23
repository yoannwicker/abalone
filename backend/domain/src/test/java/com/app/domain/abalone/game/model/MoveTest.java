package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.SquarePosition.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import org.junit.jupiter.api.Test;

class MoveTest {

  @Test
  void should_build_it() {
    // given
    Pawn firstPawn = new Pawn(Player.BLACK, C5);
    Pawn secondPawn = new Pawn(Player.BLACK, D6);
    Pawn thirdPawn = new Pawn(Player.BLACK, E7);

    assertThatCode(
            () -> new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y))
        .doesNotThrowAnyException();
  }

  @Test
  void should_contains_pawns() {
    assertThatThrownBy(() -> new Move(Set.of(), Direction.MOVE_FORWARD_Y))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_not_contains_more_than_three_pawns() {
    // given
    Pawn firstPawn = new Pawn(Player.BLACK, C5);
    Pawn secondPawn = new Pawn(Player.BLACK, D6);
    Pawn thirdPawn = new Pawn(Player.BLACK, E7);
    Pawn fouthPawn = new Pawn(Player.BLACK, F7);

    assertThatThrownBy(
            () ->
                new Move(
                    Set.of(firstPawn, secondPawn, thirdPawn, fouthPawn), Direction.MOVE_FORWARD_Y))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_not_contains_pawns_in_different_line() {
    // given
    Pawn firstPawn = new Pawn(Player.BLACK, C6);
    Pawn secondPawn = new Pawn(Player.BLACK, D6);
    Pawn thirdPawn = new Pawn(Player.BLACK, E7);

    assertThatThrownBy(
            () -> new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_not_contains_unfollowed_pawns_in_same_line() {
    // given
    Pawn firstPawn = new Pawn(Player.BLACK, C5);
    Pawn secondPawn = new Pawn(Player.BLACK, D6);
    Pawn thirdPawn = new Pawn(Player.BLACK, F7);

    assertThatThrownBy(
            () -> new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_not_contains_pawns_with_different_colors() {
    // given
    Pawn firstPawn = new Pawn(Player.BLACK, C6);
    Pawn secondPawn = new Pawn(Player.WHITE, D6);
    Pawn thirdPawn = new Pawn(Player.BLACK, E7);

    assertThatThrownBy(
            () -> new Move(Set.of(firstPawn, secondPawn, thirdPawn), Direction.MOVE_FORWARD_Y))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
