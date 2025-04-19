package com.app.domain.game;

import java.util.Set;

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
        findPawnInFront(move, oneOfPawnsToMove);

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

    private void findPawnInFront(Move move, Pawn oneOfPawnsToMove) {
        var movedPawn = oneOfPawnsToMove.move(move.direction());
        movedPawn.ifPresent(pawn -> {
            if (move.pawns().contains(pawn)) {
                findPawnInFront(move, pawn);
                return;
            }
            if (blackPawns.contains(pawn) || whitePawns.contains(pawn)) {
                throw new IllegalStateException("Cannot move pawn to occupied square at " + pawn);
            }
        });
        // TODO: eliminaton du pion
        // TODO: ajouter les pions blancs identifies aux pions a deplacer
    }
}
