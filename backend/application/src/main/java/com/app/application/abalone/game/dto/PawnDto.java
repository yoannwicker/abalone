package com.app.application.abalone.game.dto;

import com.app.domain.abalone.game.model.Pawn;
import com.app.domain.abalone.game.model.Player;
import com.app.domain.abalone.game.model.SquarePosition;

public record PawnDto(Player playerOwner, String position) {

  public static PawnDto fromDomain(Pawn pawn) {
    return new PawnDto(pawn.player(), pawn.squarePosition().toString());
  }

  public Pawn toDomain() {
    return new Pawn(playerOwner, SquarePosition.valueOf(position));
  }
}
