package com.app.application.abalone.game.dto;

import com.app.domain.abalone.game.model.Direction;
import com.app.domain.abalone.game.model.Move;
import com.app.domain.abalone.game.model.Player;
import java.util.Set;
import java.util.stream.Collectors;

public record MoveDto(Player player, Set<PawnDto> pawnsToMove, String direction) {

  private static Direction domainDirection(String direction) {
    return switch (direction) {
      case "E" -> Direction.MOVE_FORWARD_X;
      case "W" -> Direction.MOVE_BACK_X;
      case "SE" -> Direction.MOVE_BACK_Z;
      case "NW" -> Direction.MOVE_FORWARD_Z;
      case "SW" -> Direction.MOVE_BACK_Y;
      case "NE" -> Direction.MOVE_FORWARD_Y;
      default -> throw new IllegalArgumentException("Invalid direction: " + direction);
    };
  }

  public Move toDomain() {
    return new Move(
        pawnsToMove.stream().map(PawnDto::toDomain).collect(Collectors.toSet()),
        domainDirection(direction));
  }
}
