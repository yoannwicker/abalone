package com.app.domain.game;

public class Game {
    private Player playerTurn = Player.BLACK;
    private GameBoard gameBoard;

    public void play(Player player, Move move) {
        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }
        playerTurn = player.nextPlayer();
    }
}
