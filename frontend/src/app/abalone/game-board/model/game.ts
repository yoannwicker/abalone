import {Pawn} from "./pawn";
import {Square} from "./square";
import {Player} from "./player";

export class Game {
  private rows = [
    ['I5', 'I6', 'I7', 'I8', 'I9'],
    ['H4', 'H5', 'H6', 'H7', 'H8', 'H9'],
    ['G3', 'G4', 'G5', 'G6', 'G7', 'G8', 'G9'],
    ['F2', 'F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9'],
    ['E1', 'E2', 'E3', 'E4', 'E5', 'E6', 'E7', 'E8', 'E9'],
    ['D1', 'D2', 'D3', 'D4', 'D5', 'D6', 'D7', 'D8'],
    ['C1', 'C2', 'C3', 'C4', 'C5', 'C6', 'C7'],
    ['B1', 'B2', 'B3', 'B4', 'B5', 'B6'],
    ['A1', 'A2', 'A3', 'A4', 'A5'],
  ];

  constructor(
    public playerTurn: Player,
    public blackPawnPositions: string[],
    public whitePawnPositions: string[]
  ) {
  }

  getSquareByRows(): Square[][] {
    return this.rows.map(row => row.map(position => new Square(
      position,
      false,
      this.getPawn(position)
    )));
  }

  private getPawn(position: string): Pawn | null {
    if (this.blackPawnPositions.includes(position)) {
      return Pawn.blackPawn(position);
    }
    if (this.whitePawnPositions.includes(position)) {
      return Pawn.whitePawn(position);
    }
    return null;
  }

}
