package com.app.domain.game;

import java.util.Set;

public class Game {
    private Player playerTurn;
    private Set<Pawn> blackPawns;
    private Set<Pawn> whitePawns;

    public Game() {
        this.playerTurn = Player.BLACK;
        this.blackPawns = Pawn.createBlackPawns();
        this.whitePawns = Pawn.createWhitePawns();
    }

    public Pawn play(Player player, Move move) {
        // TODO : deplacer Player dans Pawn
        // TODO : renommer Player par Color

        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }

        if (Player.BLACK.equals(player) && !blackPawns.contains(move.pawn())
        || Player.WHITE.equals(player) && !whitePawns.contains(move.pawn())) {
            throw new IllegalArgumentException();
        }

        var movedPawn = move.movePawn();

        if (Player.BLACK.equals(player)) {
            blackPawns.remove(move.pawn());
            blackPawns.add(movedPawn);
        }

        if (Player.WHITE.equals(player)) {
            whitePawns.remove(move.pawn());
            whitePawns.add(movedPawn);
        }

        playerTurn = player.nextPlayer();
        return movedPawn;
    }
}
