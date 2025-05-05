import {Pawn} from "./pawn";
import {Player} from "./player";

export class Square {
  constructor(public position: string, public selected: boolean, public pawn: Pawn | null) {
  }

  get positionLine(): number {
    const letter = this.position.charAt(0);
    return letter.charCodeAt(0) - 'A'.charCodeAt(0);
  }

  get positionNumber(): number {
    return parseInt(this.position.substring(1));
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

  intermediatePositionWith(square: Square): string | null {
    if (this.isSeparatedBy(2, square)) {
      const targetLineNumber = (this.positionLine + square.positionLine) / 2;
      const targetLine = String.fromCharCode('A'.charCodeAt(0) + targetLineNumber);
      const targetNumber = (this.positionNumber + square.positionNumber) / 2;
      return targetLine + targetNumber;
    }

    return null;
  }

  isAdjacentOf(square: Square | undefined): boolean {
    if (!square) {
      return false;
    }
    return this.isSeparatedBy(1, square);
  }

  private isSeparatedBy(distance: number, square: Square): boolean {
    const positionLineDiff = Math.abs(this.positionLine - square.positionLine);
    const positionNumberDiff = Math.abs(this.positionNumber - square.positionNumber);

    return this.positionLine === square.positionLine && positionNumberDiff === distance
      || this.positionNumber === square.positionNumber && positionLineDiff === distance
      || positionNumberDiff === distance && positionLineDiff === distance;
  }
}
