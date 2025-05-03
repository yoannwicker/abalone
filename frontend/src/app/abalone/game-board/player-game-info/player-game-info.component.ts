import {Component, effect, input} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Player} from "../model/player";

@Component({
  selector: 'app-player-game-info',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './player-game-info.component.html',
  styleUrl: './player-game-info.component.css'
})
export class PlayerGameInfoComponent {
  capturedPawns = input.required<number>();
  player = input.required<Player>();

  protected capturedPawnsForDisplay: boolean[] = [];
  protected readonly Player = Player;

  constructor() {
    effect(() => {
      this.capturedPawnsForDisplay = Array(6).fill(false).map((_, i) => i < this.capturedPawns());
    });
  }
}
