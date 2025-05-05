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
    const positionLineDiff = Math.abs(this.positionLine - square.positionLine);
    const positionNumberDiff = Math.abs(this.positionNumber - square.positionNumber);

    if (this.positionLine === square.positionLine && positionNumberDiff === 2
      || this.positionNumber === square.positionNumber && positionLineDiff === 2
      || positionNumberDiff === 2 && positionLineDiff === 2) {
      const targetLineNumber = (this.positionLine + square.positionLine) / 2;
      const targetLine = String.fromCharCode('A'.charCodeAt(0) + targetLineNumber);
      const targetNumber = (this.positionNumber + square.positionNumber) / 2;
      return targetLine + targetNumber;
    }

    return null;
  }
}
