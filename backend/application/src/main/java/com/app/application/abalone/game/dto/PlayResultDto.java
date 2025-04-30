package com.app.application.abalone.game.dto;

import com.app.domain.abalone.game.model.PlayResult;
import java.util.Set;
import java.util.stream.Collectors;

public record PlayResultDto(Set<PawnDto> pawns, int blackPawnsLost, int whitePawnsLost) {

  public static PlayResultDto fromDomain(PlayResult playResult) {
    return new PlayResultDto(
        playResult.movedPawns().stream().map(PawnDto::fromDomain).collect(Collectors.toSet()),
        playResult.blackPawnsLost(),
        playResult.whitePawnsLost());
  }
}
