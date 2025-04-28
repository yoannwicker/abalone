import {Player} from "./player";

export class Pawn {
  constructor(public playerOwner: Player, public position: string) {
  }

  static blackPawn(position: string) {
    return new Pawn(Player.BLACK, position);
  }

  static whitePawn(position: string) {
    return new Pawn(Player.WHITE, position);
  }
}
