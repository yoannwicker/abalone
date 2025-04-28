import {Pawn} from "./pawn";
import {Player} from "./player";

export class Square {
  constructor(public position: string, public selected: boolean, public pawn: Pawn | null) {
  }

  hasBlackPawn(): boolean {
    return this.hasPawnBelongingPlayer(Player.BLACK);
  }

  hasWhitePawn(): boolean {
    return this.hasPawnBelongingPlayer(Player.WHITE);
  }

  hasPawnBelongingPlayer(player: Player | undefined): boolean {
    return this?.pawn ? this.pawn.playerOwner === player : false;
  }
}
