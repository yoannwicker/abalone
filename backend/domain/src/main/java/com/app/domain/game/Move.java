package com.app.domain.game;

public record Move(SquarePosition initialPosition, Direction direction) {

    public SquarePosition futurePosition() {
        return switch (direction) {
            case FORWARD_X -> initialPosition.moveOnX(2);
            case FORWARD_Y -> initialPosition.moveOnY(2);
            case FORWARD_Z -> initialPosition.moveOnZ(2);
        };
    }
}
