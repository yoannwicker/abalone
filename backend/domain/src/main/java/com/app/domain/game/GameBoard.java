package com.app.domain.game;

import static com.app.domain.game.SquarePosition.*;

public class GameBoard {
    /* TODO : idee possible
        - avoir une liste de position de "square" valides
        - gerer 2 listes de pions dans Game
        - GameBoard servirait alors soit de validateur uniquement (reduire les tests au passage)
        soit la meme utilisation que actuellement
     */
    Square[][][] squares = new Square[17][17][17];

    public GameBoard() {
        for (int x = 0; x < 17; x++) {
            for (int y = 0; y < 17; y++) {
                for (int z = 0; z < 17; z++) {
                    if (isStartingPositionsOfBlackPawns(x, y, z)) {
                        squares[x][y][z] = Square.BLACK_PAWN;
                    }
                    if (isStartingPositionsOfWhitePawns(x, y, z)) {
                        squares[x][y][z] = Square.WHITE_PAWN;
                    }
                    if (isStartingEmptyPositions(x, y, z)) {
                        squares[x][y][z] = Square.EMPTY;
                    }
                }
            }
        }
    }

    public Square getSquare(SquarePosition squarePosition) {
        return squares[squarePosition.x()][squarePosition.y()][squarePosition.z()];
    }

    public void setSquare(SquarePosition squarePosition, Square square) {
        squares[squarePosition.x()][squarePosition.y()][squarePosition.z()] = square;
    }
}
