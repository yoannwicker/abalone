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

    if (move.isSideways()
        && move.movePawns().stream()
            .anyMatch(
                pawnProjection ->
                    blackPlayerPawns.hasPawnAtSamePosition(pawnProjection)
                        || whitePlayerPawns.hasPawnAtSamePosition(pawnProjection))) {
      throw new IllegalStateException("Cannot move sideways to occupied square");
    }

    Set<Pawn> opponentPawnsToPush =
        move.isSideways() ? emptySet() : collectOpponentPawnsToPush(move);

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

  // TODO: cette methode fait trop de chose : a renommer ou a inline + split
  private Set<Pawn> collectOpponentPawnsToPush(Move move) {
    var pawnProjection = move.getPawnInFront().projected(move.direction());
    return pawnProjection
        .map(
            pawn -> {
              if (blackPlayerPawns.hasPawnAtSamePosition(pawn)
                  || whitePlayerPawns.hasPawnAtSamePosition(pawn)) {
                var opponentPawn = pawn.toOpponentPawn();
                if (blackPlayerPawns.contains(opponentPawn)
                    || whitePlayerPawns.contains(opponentPawn)) {
                  boolean isGroupMove = move.pawns().size() > 1;
                  Optional<Pawn> opponentPawnProjection = opponentPawn.projected(move.direction());
                  boolean nextSquareOfOpponentPawnFree =
                      opponentPawnProjection
                          .map(
                              next ->
                                  !whitePlayerPawns.hasPawnAtSamePosition(next)
                                      && !blackPlayerPawns.hasPawnAtSamePosition(next))
                          .orElse(true);

                  if (isGroupMove && nextSquareOfOpponentPawnFree) {
                    return Set.of(opponentPawn);
                  }
                  Optional<Pawn> nextOpponentPawnProjection =
                      opponentPawnProjection.flatMap(next -> next.projected(move.direction()));
                  boolean nextSquareOfNextOpponentPawnFree =
                      nextOpponentPawnProjection
                          .map(
                              next ->
                                  !whitePlayerPawns.hasPawnAtSamePosition(next)
                                      && !blackPlayerPawns.hasPawnAtSamePosition(next))
                          .orElse(true);
                  if (move.pawns().size() > 2 && nextSquareOfNextOpponentPawnFree) {
                    return Set.of(opponentPawn, opponentPawnProjection.orElseThrow());
                  }
                }
                throw new IllegalStateException("Cannot move pawn to occupied square at " + pawn);
              }
              return new HashSet<Pawn>();
            })
        .orElse(new HashSet<>());
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
