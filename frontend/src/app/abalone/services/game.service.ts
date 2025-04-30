import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Game} from "../game-board/model/game";
import {Pawn} from "../game-board/model/pawn";
import {Player} from "../game-board/model/player";
import {MoveResult} from "../game-board/model/move-result";

@Injectable({
  providedIn: 'root'
})
export class GameService {
  // TODO : replace auth by game
  private newGameUrl = '/api/auth/new';
  private movePawnGameUrl = '/api/auth/move';

  constructor(private http: HttpClient) {
  }

  getInitialPawnPositions(): Observable<Game> {
    return this.http.get<Game>(this.newGameUrl);
  }

  // TODO : besoin du joueur ?
  movePawn(player: Player | undefined, pawnsToMove: Pawn[], direction: string): Observable<MoveResult> {
    return this.http.post<MoveResult>(this.movePawnGameUrl, {
      player: player,
      pawnsToMove: pawnsToMove,
      direction: direction
    });
  }
}
