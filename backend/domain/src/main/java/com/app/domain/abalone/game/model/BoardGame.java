package com.app.domain.abalone.game.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record BoardGame(PlayerPawns blackPlayerPawns, PlayerPawns whitePlayerPawns) {
  public BoardGame {
    if (blackPlayerPawns == null || whitePlayerPawns == null) {
      throw new IllegalArgumentException("Black and white player pawns cannot be null");
    }
  }

  private Set<Pawn> allMovedPawns(
      OpponentPawnsToPush opponentPawnsToPush, PawnsToMove pawnsToMove) {
    return Stream.of(pawnsToMove.futurePawns(), opponentPawnsToPush.futurePawns())
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  public GameStatus status() {
    return new GameStatus(blackPlayerPawns.lostPawnsCount(), whitePlayerPawns.lostPawnsCount());
  }

  public Set<Pawn> update(Player player, Move move) {
    if (blackPlayerPawns.illegalMove(move) || whitePlayerPawns.illegalMove(move)) {
      throw new IllegalArgumentException("Illegal move " + move);
    }

    PawnsToMove pawnsToMove = move.pawnsToMove();

    Optional<OpponentPawnsToPush> opponentPawnsToPush = collectOpponentPawnsToPush(pawnsToMove);

    movePawnsOnThBoard(player, pawnsToMove, opponentPawnsToPush);

    return opponentPawnsToPush
        .map(opponentPawns -> allMovedPawns(opponentPawns, pawnsToMove))
        .orElse(pawnsToMove.futurePawns());
  }

  private void movePawnsOnThBoard(
      Player player, PawnsToMove pawnsToMove, Optional<OpponentPawnsToPush> opponentPawnsToPush) {
    if (Player.BLACK.equals(player)) {
      blackPlayerPawns.update(pawnsToMove);
      opponentPawnsToPush.ifPresent(whitePlayerPawns::update);
    }
    if (Player.WHITE.equals(player)) {
      whitePlayerPawns.update(pawnsToMove);
      opponentPawnsToPush.ifPresent(blackPlayerPawns::update);
    }
  }

  private boolean hasPawnOverlapFuturePosition(PawnsToMove pawnsToMove) {
    return blackPlayerPawns.hasPawnOverlapPosition(pawnsToMove)
        || whitePlayerPawns.hasPawnOverlapPosition(pawnsToMove);
  }

  private boolean canMoveWithoutPushingOpponent(PawnsToMove pawnsToMove) {
    return !hasPawnOverlapFuturePosition(pawnsToMove);
  }

  private Optional<OpponentPawnsToPush> collectOpponentPawnsToPush(PawnsToMove pawnsToMove) {

    if (canMoveWithoutPushingOpponent(pawnsToMove)) {
      return Optional.empty();
    }

    OpponentPawnsToPush opponentPawnsToPush = pawnsToMove.pawnsToPushProjection();
    if (hasOpponentPawnOverlapFuturePositionOf(pawnsToMove)
        && freeNextSquareOfOpponentPawns(opponentPawnsToPush)) {
      return Optional.of(opponentPawnsToPush);
    }

    throw new IllegalStateException(
        "Cannot move pawn to occupied square at " + pawnsToMove.getPawnInFrontProjection());
  }

  private boolean hasOpponentPawnOverlapFuturePositionOf(PawnsToMove pawnsToMove) {
    return blackPlayerPawns.hasOpponentPawnOverlapFuturePositionOf(pawnsToMove)
        || whitePlayerPawns.hasOpponentPawnOverlapFuturePositionOf(pawnsToMove);
  }

  private Boolean freeNextSquareOfOpponentPawns(OpponentPawnsToPush opponentPawnsToPush) {
    return hasNoPawnOverlapFuturePositionOf(opponentPawnsToPush.nextSquareOfFirstPawnToPush())
        || opponentPawnsToPush.canPushTwoPawns()
            && hasNoPawnOverlapFuturePositionOf(opponentPawnsToPush.nextSquareOfSecondPawnToPush());
  }

  private Boolean hasNoPawnOverlapFuturePositionOf(Optional<SquarePosition> opponentPawnsToPush) {
    return opponentPawnsToPush
        .map(
            nextSquare ->
                whitePlayerPawns.hasNoPawnOverlapPosition(nextSquare)
                    && blackPlayerPawns.hasNoPawnOverlapPosition(nextSquare))
        .orElse(true);
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
}
