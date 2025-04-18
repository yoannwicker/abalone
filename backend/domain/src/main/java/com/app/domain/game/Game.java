package com.app.domain.game;

import java.util.Optional;
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

    public Optional<Pawn> play(Player player, Move move) {

        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }

        if (Player.BLACK.equals(player) && !blackPawns.contains(move.pawn())
        || Player.WHITE.equals(player) && !whitePawns.contains(move.pawn())) {
            throw new IllegalArgumentException();
        }

        var movedPawn = move.movePawn();
        movedPawn.ifPresent(pawn -> {
            if (blackPawns.contains(pawn) || whitePawns.contains(pawn)) {
                throw new IllegalStateException("Cannot move pawn to occupied square at " + pawn);
            }
        });

        if (Player.BLACK.equals(player)) {
            blackPawns.remove(move.pawn());
            movedPawn.ifPresent(blackPawns::add);
        }

        if (Player.WHITE.equals(player)) {
            whitePawns.remove(move.pawn());
            movedPawn.ifPresent(whitePawns::add);
        }

        playerTurn = player.nextPlayer();
        return movedPawn;
    }
}
