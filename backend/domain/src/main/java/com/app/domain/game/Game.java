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

        var initialSquareContent = gameBoard.getSquare(move.initialPosition());
        if (player.equals(Player.BLACK) && initialSquareContent != Square.BLACK_PAWN
                || player.equals(Player.WHITE) && initialSquareContent != Square.WHITE_PAWN) {
            throw new IllegalArgumentException();
        }

        // TODO : suivre TODO GameBoard car ca ne marche pas en l'etat
        var futurSquareContent = gameBoard.getSquare(move.futurePosition());

        playerTurn = player.nextPlayer();
    }
}
