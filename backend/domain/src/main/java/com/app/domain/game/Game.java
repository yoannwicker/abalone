package com.app.domain.game;

public class Game {
    private Player playerTurn;
    private GameBoard gameBoard;

    public Game() {
        this.playerTurn = Player.BLACK;
        this.gameBoard = new GameBoard();
    }

    public void play(Player player, Move move) {
        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }


        playerTurn = player.nextPlayer();
    }
}
