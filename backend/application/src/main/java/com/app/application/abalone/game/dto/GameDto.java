package com.app.application.abalone.game.dto;

import com.app.domain.abalone.game.model.Game;
import java.util.Set;

public record GameDto(
    String playerTurn, Set<String> blackPawnPositions, Set<String> whitePawnPositions) {

  public static GameDto fromGame(Game newGame) {
    return new GameDto(
        newGame.getPlayerTurn().toString(),
        newGame.getBlackPawnPositions(),
        newGame.getWhitePawnPositions());
  }
}
