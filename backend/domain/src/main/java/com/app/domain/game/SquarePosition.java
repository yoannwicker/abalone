package com.app.domain.game;

// TODO: renommer pour Position ?
public record SquarePosition(int x, int y, int z) {

    // TODO: initialiser une liste de pion avec les positions ci-dessous
    // TODO: ajouter la couleur pour chaque pion

    public SquarePosition update(Direction direction) {
        return switch (direction) {
            case MOVE_FORWARD_X -> new SquarePosition(x + 2, y + 1, z - 1);
            case MOVE_BACK_X -> new SquarePosition(x - 2, y - 1, z + 1);
            case MOVE_FORWARD_Y -> new SquarePosition(x + 1, y + 2, z + 1);
            case MOVE_BACK_Y -> new SquarePosition(x - 1, y - 2, z - 1);
            case MOVE_FORWARD_Z -> new SquarePosition(x - 1, y + 1, z + 2);
            case MOVE_BACK_Z -> new SquarePosition(x + 1, y - 1, z - 2);
        };
    }
}
