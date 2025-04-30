import {Pawn} from "./pawn";

export class MoveResult {
  constructor(
    public pawns: Pawn[],
    public blackPawnsLost: number,
    public whitePawnsLost: number
  ) {
  }
}
