package com.app.domain.game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {
    private Player playerTurn;
    private final Set<Pawn> blackPawns;
    private final Set<Pawn> whitePawns;

    public Game() {
        this.playerTurn = Player.BLACK;
        this.blackPawns = Pawn.createBlackPawns();
        this.whitePawns = Pawn.createWhitePawns();
    }

    public Game(Player playerTurn, Set<Pawn> blackPawns, Set<Pawn> whitePawns) {
        this.playerTurn = playerTurn;
        this.blackPawns = blackPawns;
        this.whitePawns = whitePawns;
    }

    public Set<Pawn> play(Player player, Move move) {

        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }

        if (Player.BLACK.equals(player) && move.pawns().stream().noneMatch(blackPawns::contains)
        || Player.WHITE.equals(player) && move.pawns().stream().noneMatch(whitePawns::contains)) {
            throw new IllegalArgumentException();
        }

        var oneOfPawnsToMove = move.pawns().stream().findFirst().orElseThrow();
        collectFrontPawnsToPush(move, oneOfPawnsToMove);

        var movedPawns = move.movePawns();

        if (Player.BLACK.equals(player)) {
            blackPawns.removeAll(move.pawns());
            blackPawns.addAll(movedPawns);
        }

        if (Player.WHITE.equals(player)) {
            whitePawns.removeAll(move.pawns());
            whitePawns.addAll(movedPawns);
        }

        playerTurn = player.nextPlayer();
        return movedPawns;
    }

    private Set<Pawn> collectFrontPawnsToPush(Move move, Pawn oneOfPawnsToMove) {
        var movedPawn = oneOfPawnsToMove.move(move.direction());
        return movedPawn.map(pawn -> {
            if (move.pawns().contains(pawn)) {
                collectFrontPawnsToPush(move, pawn);
                return new HashSet<Pawn>();
            }
            if (blackPawns.contains(pawn) || whitePawns.contains(pawn)) {
                // TODO : blackPawns.contains(pawn) si player WHITE
                if (whitePawns.contains(pawn)) {
                    boolean isGroupMove = move.pawns().size() > 1;
                    Optional<Pawn> pawnProjection = pawn.move(move.direction());
                    boolean nextSquareFree = pawnProjection
                            .map(next -> !whitePawns.contains(next) && !blackPawns.contains(next))
                            .orElse(true);

                    if (isGroupMove && nextSquareFree) {
                        return pawnProjection.stream().collect(Collectors.toSet());
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
