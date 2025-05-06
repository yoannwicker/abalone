package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.PlayerPawns.initalBlackPlayerPawns;
import static com.app.domain.abalone.game.model.PlayerPawns.initalWhitePlayerPawns;

import java.util.*;

public class Game {

  private final BoardGame boardGame;
  private GameStatus gameStatus;
  private Player playerTurn;

  public Game() {
    this.playerTurn = Player.BLACK;
    this.boardGame = new BoardGame(initalBlackPlayerPawns(), initalWhitePlayerPawns());
    this.gameStatus = boardGame.status();
  }

  public Game(Player playerTurn, Set<Pawn> blackPawns, Set<Pawn> whitePawns) {
    this.playerTurn = playerTurn;
    this.boardGame =
        new BoardGame(
            new PlayerPawns(Player.BLACK, blackPawns), new PlayerPawns(Player.WHITE, whitePawns));
    this.gameStatus = boardGame.status();
  }

  public Set<String> getBlackPawnPositions() {
    return boardGame.getBlackPawnPositions();
  }

  public Set<String> getWhitePawnPositions() {
    return boardGame.getWhitePawnPositions();
  }

  public Player getPlayerTurn() {
    return playerTurn;
  }

  public PlayResult play(Player player, Move move) {

    if (player != playerTurn) {
      throw new IllegalArgumentException(player + " cannot play, it's " + playerTurn + "'s turn");
    }
    if (gameStatus.end()) {
      throw new IllegalStateException("Game is already finished");
    }

    Set<Pawn> allMovedPawns = boardGame.update(player, move);

    var playResult =
        new PlayResult(
            allMovedPawns,
            boardGame.blackPlayerPawns().lostPawnsCount(),
            boardGame.whitePlayerPawns().lostPawnsCount());

    playerTurn = player.nextPlayer();
    gameStatus = playResult.gameStatus();

    return playResult;
  }

  public PlayResult drawMatch() {
    if (this.gameStatus.end()) {
      throw new IllegalStateException("Game is already finished");
    }
    this.gameStatus = GameStatus.draw();
    return new PlayResult(
        Set.of(),
        boardGame.blackPlayerPawns().lostPawnsCount(),
        boardGame.whitePlayerPawns().lostPawnsCount(),
        this.gameStatus);
  }
}
