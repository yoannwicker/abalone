package com.app.domain.game;

public class Game {
    private Player playerTurn;

    public Game() {
        this.playerTurn = Player.BLACK;
    }

    public Pawn play(Player player, Move move) {
        // TODO : deplacer Player dans Pawn
        // TODO : renommer Player par Color

        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }

        var movedPawn = move.movePawn();

        playerTurn = player.nextPlayer();
        return movedPawn;
    }
}
