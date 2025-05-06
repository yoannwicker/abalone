package com.app.domain.abalone.game.model;

import static com.app.domain.abalone.game.model.PlayerPawns.initalBlackPlayerPawns;
import static com.app.domain.abalone.game.model.PlayerPawns.initalWhitePlayerPawns;
import static java.util.Collections.emptySet;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

  final PlayerPawns blackPlayerPawns;
  final PlayerPawns whitePlayerPawns;
  private Player playerTurn;
  private GameStatus gameStatus;

  public Game() {
    this.playerTurn = Player.BLACK;
    this.blackPlayerPawns = initalBlackPlayerPawns();
    this.whitePlayerPawns = initalWhitePlayerPawns();
    this.gameStatus =
        new GameStatus(blackPlayerPawns.lostPawnsCount(), whitePlayerPawns.lostPawnsCount());
  }

  public Game(Player playerTurn, Set<Pawn> blackPawns, Set<Pawn> whitePawns) {
    this.playerTurn = playerTurn;
    this.blackPlayerPawns = new PlayerPawns(Player.BLACK, blackPawns);
    this.whitePlayerPawns = new PlayerPawns(Player.WHITE, whitePawns);
    this.gameStatus =
        new GameStatus(blackPlayerPawns.lostPawnsCount(), whitePlayerPawns.lostPawnsCount());
  }

  public Set<String> getBlackPawnPositions() {
    return blackPlayerPawns.pawns().stream()
        .map(Pawn::squarePosition)
        .map(SquarePosition::name)
        .collect(Collectors.toSet());
  }

  public Set<String> getWhitePawnPositions() {
    return whitePlayerPawns.pawns().stream()
        .map(Pawn::squarePosition)
        .map(SquarePosition::name)
        .collect(Collectors.toSet());
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
    if (blackPlayerPawns.illegalMove(move) || whitePlayerPawns.illegalMove(move)) {
      throw new IllegalArgumentException("Illegal move " + move);
    }

    if (move.isLateralMove() && move.movePawns().stream().anyMatch(this::isPositionOccupied)) {
      throw new IllegalStateException("Cannot move sideways to occupied square");
    }

    var maybePawnInFrontProjection = move.getPawnInFront().projected(move.direction());
    var isPawnInFrontProjectionFree =
        !maybePawnInFrontProjection.map(this::isPositionOccupied).orElse(false);
    boolean canMoveWithoutPushingOpponent = move.isLateralMove() || isPawnInFrontProjectionFree;
    Set<Pawn> opponentPawnsToPush =
        canMoveWithoutPushingOpponent
            ? emptySet()
            : maybePawnInFrontProjection
                .map(
                    pawnInFrontProjection ->
                        collectOpponentPawnsToPush(move, pawnInFrontProjection))
                .orElseThrow();

    var movedPawns = move.movePawns();
    var movedOpponentPawns =
        opponentPawnsToPush.stream()
            .map(opponentPawnToPush -> new Move(opponentPawnToPush, move.direction()).movePawns())
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

    if (Player.BLACK.equals(player)) {
      blackPlayerPawns.update(move.pawns(), movedPawns);
      whitePlayerPawns.update(opponentPawnsToPush, movedOpponentPawns);
    }
    if (Player.WHITE.equals(player)) {
      whitePlayerPawns.update(move.pawns(), movedPawns);
      blackPlayerPawns.update(opponentPawnsToPush, movedOpponentPawns);
    }

    var allMovedPawns =
        Stream.of(movedPawns, movedOpponentPawns)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    var playResult =
        new PlayResult(
            allMovedPawns, blackPlayerPawns.lostPawnsCount(), whitePlayerPawns.lostPawnsCount());

    playerTurn = player.nextPlayer();
    gameStatus = playResult.gameStatus();

    return playResult;
  }

  private Set<Pawn> collectOpponentPawnsToPush(Move move, Pawn pawnInFrontProjection) {
    var opponentPawn = pawnInFrontProjection.toOpponentPawn();
    if (blackPlayerPawns.contains(opponentPawn) || whitePlayerPawns.contains(opponentPawn)) {
      Optional<Pawn> opponentPawnProjection = opponentPawn.projected(move.direction());
      if (move.pawns().size() > 1 && getNextSquareOfOpponentPawnFree(opponentPawnProjection)) {
        return Set.of(opponentPawn);
      }

      Optional<Pawn> nextOpponentPawnProjection =
          opponentPawnProjection.flatMap(pawn -> pawn.projected(move.direction()));
      if (move.pawns().size() > 2 && getNextSquareOfOpponentPawnFree(nextOpponentPawnProjection)) {
        return Set.of(opponentPawn, opponentPawnProjection.orElseThrow());
      }
    }
    throw new IllegalStateException(
        "Cannot move pawn to occupied square at " + pawnInFrontProjection);
  }

  private boolean isPositionOccupied(Pawn pawnProjection) {
    return blackPlayerPawns.hasPawnAtSamePosition(pawnProjection)
        || whitePlayerPawns.hasPawnAtSamePosition(pawnProjection);
  }

  private Boolean getNextSquareOfOpponentPawnFree(Optional<Pawn> opponentPawnProjection) {
    return opponentPawnProjection
        .map(
            next ->
                !whitePlayerPawns.hasPawnAtSamePosition(next)
                    && !blackPlayerPawns.hasPawnAtSamePosition(next))
        .orElse(true);
  }

  public PlayResult drawMatch() {
    if (this.gameStatus.end()) {
      throw new IllegalStateException("Game is already finished");
    }
    this.gameStatus = GameStatus.draw();
    return new PlayResult(
        Set.of(),
        blackPlayerPawns.lostPawnsCount(),
        whitePlayerPawns.lostPawnsCount(),
        this.gameStatus);
  }
}
