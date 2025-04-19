package com.app.domain.game;

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

    public Set<Pawn> play(Player player, Move move) {

        if (player != playerTurn) {
            throw new IllegalArgumentException();
        }

        if (Player.BLACK.equals(player) && move.pawns().stream().noneMatch(blackPawns::contains)
        || Player.WHITE.equals(player) && move.pawns().stream().noneMatch(whitePawns::contains)) {
            throw new IllegalArgumentException();
        }

        var movedPawns = move.movePawn();
        movedPawns.forEach(pawn -> {
            if (blackPawns.contains(pawn) || whitePawns.contains(pawn)) {
                throw new IllegalStateException("Cannot move pawn to occupied square at " + pawn);
            }
        });

        if (Player.BLACK.equals(player)) {
            blackPawns.removeAll(move.pawns());
            blackPawns.addAll(movedPawns);
        }

        if (Player.WHITE.equals(player)) {
            whitePawns.removeAll(move.pawns());
            whitePawns.addAll(movedPawns);
        }

        playerTurn = player.nextPlayer();
        return movedPawns;
    }
}
