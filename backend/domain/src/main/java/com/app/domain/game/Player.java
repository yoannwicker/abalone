package com.app.domain.game;

public enum Player {
    BLACK,
    WHITE;

    Player nextPlayer() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }
}
