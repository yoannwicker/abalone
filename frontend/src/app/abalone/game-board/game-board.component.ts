import {Component, OnInit} from '@angular/core';
import {CommonModule, NgFor, NgIf} from '@angular/common';
import {Game} from "./model/game";
import {GameService} from "../services/game.service";
import {Square} from "./model/square";
import {Player} from "./model/player";
import {Pawn} from "./model/pawn";
import {MoveResult} from "./model/move-result";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SquareComponent} from "./square/square.component";
import {PlayerGameInfoComponent} from "./player-game-info/player-game-info.component";

@Component({
  selector: 'app-game-board',
  standalone: true,
  imports: [NgFor, NgIf,
    CommonModule, SquareComponent, PlayerGameInfoComponent],
  templateUrl: './game-board.component.html',
  styleUrl: './game-board.component.css'
})
export class GameBoardComponent implements OnInit {
  board: Square[][] = [];
  selectedPositions: string[] = [];
  playerTurn: Player | undefined;
  hoveredDirection: string | null = null;
  lastMovedPositions: string[] = [];
  winner: Player | null = null;
  protected blackPawnsLost: number = 0;
  protected whitePawnsLost: number = 0;
  protected readonly Player = Player;
  private maxSelectedPawns: number = 3;

  constructor(private gameService: GameService, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.initBoard();
  }

  selectCell(cell: Square) {
    if (this.selectedPositions.length >= this.maxSelectedPawns && !cell.selected
      || !cell.hasPawnBelongingPlayer(this.playerTurn)) {
      return;
    }
    cell.selected = !cell.selected;
    if (cell.selected) {
      this.selectedPositions.push(cell.position);
    } else {
      this.selectedPositions = this.selectedPositions.filter(position => position !== cell.position);
    }
  }

  onRightClick(event: MouseEvent) {
    event.preventDefault();
    if (this.selectedPositions) {
      this.cancelSelection();
    }
  }

  move(direction: string) {
    const pawnsToMove = this.getSquares(this.selectedPositions)
    .map(square => square.pawn)
    .filter(pawn => pawn !== null) as Pawn[];
    this.gameService.movePawn(this.playerTurn, pawnsToMove, direction).subscribe({
      next: (movedResult: MoveResult) => {
        this.getPawnSquares(pawnsToMove).forEach(
          square => {
            square.pawn = null;
          }
        );
        movedResult.pawns.forEach(
          pawn => {
            const square = this.getSquare(pawn.position);
            if (square) {
              square.pawn = pawn;
            }
          }
        );
        this.lastMovedPositions = pawnsToMove
        .map(pawn => pawn.position)
        .concat(movedResult.pawns.filter(pawn => pawn.playerOwner === this.playerTurn).map(pawn => pawn.position));
        this.winner = movedResult.winner;
        this.blackPawnsLost = movedResult.blackPawnsLost;
        this.whitePawnsLost = movedResult.whitePawnsLost;
        this.cancelSelection();
        this.nextTurn();
      },
      error: (err) => {
        console.log("Error code: ", err?.status);
        if (err?.status === 400) {
          this.snackBar.open('Vous ne pouvez pas jouer ce coup', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'right',
            verticalPosition: 'top'
          });
        } else if (err?.status === 403) {
          this.snackBar.open('Vous devez être connecté pour jouer', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'right',
            verticalPosition: 'top'
          });
        } else {
          this.snackBar.open('Une erreur est survenue technique est survenue', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'right',
            verticalPosition: 'top'
          });
        }
      }
    });
  }

  getSquare(position: string): Square | undefined {
    return this.board
    .find(row => row.some(cell => cell.position === position))
    ?.find(cell => cell.position === position);
  }

  getPawnSquares(pawns: Pawn[]): Square[] {
    const positions = pawns.map(pawn => pawn.position);
    return this.getSquares(positions);
  }

  getSquares(positions: string[]): Square[] {
    return this.board
    .flatMap(row => row)
    .filter(cell => positions.includes(cell.position));
  }

  onHover(direction: string | null) {
    this.hoveredDirection = direction;
  }

  restartGame() {
    this.winner = null;
    this.lastMovedPositions = [];
    this.initBoard();
  }

  protected otherPlayer(player: Player | undefined) {
    return player === Player.BLACK ? Player.WHITE : Player.BLACK;
  }

  private cancelSelection() {
    this.getSquares(this.selectedPositions).forEach(square => {
      square.selected = false;
    });
    this.selectedPositions = [];
  }

  private nextTurn() {
    this.playerTurn = this.otherPlayer(this.playerTurn);
  }

  private initBoard() {
    this.gameService.getInitialPawnPositions().subscribe((game: Game) => {
      const newGame = new Game(game.playerTurn, game.blackPawnPositions, game.whitePawnPositions);
      this.board = newGame.getSquareByRows();
      this.playerTurn = newGame.playerTurn;
    });
  }
}
