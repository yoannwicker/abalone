package com.app.application.abalone.game.dto;

import com.app.domain.abalone.game.model.PlayResult;
import com.app.domain.abalone.game.model.Player;
import java.util.Set;
import java.util.stream.Collectors;

public record PlayResultDto(
    Set<PawnDto> pawns, int blackPawnsLost, int whitePawnsLost, Player winner) {

  public static PlayResultDto fromDomain(PlayResult playResult) {
    return new PlayResultDto(
        playResult.movedPawns().stream().map(PawnDto::fromDomain).collect(Collectors.toSet()),
        playResult.blackPawnsLost(),
        playResult.whitePawnsLost(),
        playResult.gameStatus().winner().orElse(null));
  }
}
