package com.app.domain.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    private Player playerTurn;
    private final Pawns blackPawns;
    private final Pawns whitePawns;

    public Game() {
        this.playerTurn = Player.BLACK;
        this.blackPawns = new Pawns(Player.BLACK, Pawn.createBlackPawns());
        this.whitePawns = new Pawns(Player.WHITE, Pawn.createWhitePawns());
    }

    public Game(Player playerTurn, Set<Pawn> blackPawns, Set<Pawn> whitePawns) {
        this.playerTurn = playerTurn;
        this.blackPawns = new Pawns(Player.BLACK, blackPawns);
        this.whitePawns = new Pawns(Player.WHITE, whitePawns);
    }

    public Set<Pawn> play(Player player, Move move) {

        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }

        if (blackPawns.illegalMove(move) || whitePawns.illegalMove(move)) {
            throw new IllegalArgumentException();
        }

        var oneOfPawnsToMove = move.pawns().stream().findFirst().orElseThrow();
        var opponentPawnsToPush = collectOpponentPawnsToPush(move, oneOfPawnsToMove);

        var movedPawns = move.movePawns();
        var movedOpponentPawns = new Move(opponentPawnsToPush, move.direction()).movePawns();

        if (Player.BLACK.equals(player)) {
            blackPawns.update(move.pawns(), movedPawns);
            whitePawns.update(opponentPawnsToPush, movedOpponentPawns);
        }

        if (Player.WHITE.equals(player)) {
            whitePawns.update(move.pawns(), movedPawns);
            blackPawns.update(opponentPawnsToPush, movedOpponentPawns);
        }

        playerTurn = player.nextPlayer();
        return Stream.of(movedPawns, movedOpponentPawns)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Pawn> collectOpponentPawnsToPush(Move move, Pawn oneOfPawnsToMove) {
        var movedPawn = oneOfPawnsToMove.move(move.direction());
        return movedPawn.map(pawn -> {
            if (move.pawns().contains(pawn)) {
                return collectOpponentPawnsToPush(move, pawn);
            }
            if (blackPawns.hasPawnAtSamePosition(pawn) || whitePawns.hasPawnAtSamePosition(pawn)) {
                var opponentPawn = pawn.toOpponentPawn();
                if (blackPawns.contains(opponentPawn) || whitePawns.contains(opponentPawn)) {
                    boolean isGroupMove = move.pawns().size() > 1;
                    Optional<Pawn> opponentPawnProjection = opponentPawn.move(move.direction());
                    boolean nextSquareFree = opponentPawnProjection
                            .map(next -> !whitePawns.contains(next) && !blackPawns.contains(next))
                            .orElse(true);

                    if (isGroupMove && nextSquareFree) {
                        return Set.of(opponentPawn);
                    }
                }
                throw new IllegalStateException("Cannot move pawn to occupied square at " + pawn);
            }
            return new HashSet<Pawn>();
        }).orElse(new HashSet<>());
        // TODO: eliminaton du pion
        // TODO: ajouter les pions blancs identifies aux pions a deplacer
    }
}
