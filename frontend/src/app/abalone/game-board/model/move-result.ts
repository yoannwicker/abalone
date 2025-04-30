import {Pawn} from "./pawn";
import {Player} from "./player";

export class MoveResult {
  constructor(
    public pawns: Pawn[],
    public blackPawnsLost: number,
    public whitePawnsLost: number,
    public winner: Player | null
  ) {
  }
}
