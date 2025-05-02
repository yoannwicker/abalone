package com.app.application.abalone.game;

import com.app.application.abalone.game.dto.GameDto;
import com.app.application.abalone.game.dto.MoveDto;
import com.app.application.abalone.game.dto.PlayResultDto;
import com.app.domain.abalone.game.model.Game;
import com.app.domain.abalone.game.model.PlayResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game/")
public class GameController {

  private Game game;

  @GetMapping(value = "new", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GameDto> newGame() {
    game = new Game();
    return ResponseEntity.ok(GameDto.fromGame(game));
  }

  // TODO: Design responsive - trouver une solution pour deplacer les pions : rendre selectible les
  // cases au extermités des pions selectionnés
  // exemple : groupe selectionné = G4 - G6 ; cases selectionnables -> F3, G3, H4 du coté G4 et H7,
  // G7, F6 du coté G6
  // TODO: Design responsive - trouver une solution pour deplacer les pions : afficher les
  // directions sur les cases pour les pions exterieurs sauf les pions aliés -> plus besoin des
  // fleches
  // TODO: Utiliser Redux pour gerer l'état du jeu
  // TODO: se connecter pour jouer / Ajouter une partie de jeu (avec un id de partie)
  // TODO: Ajouter la possibilité de faire null par accord (popup à modifier)
  // TODO: Ajouter un timer / regle de null ou defaite ...etc -> url différente (choix du timer ?)
  // TODO: Refactoring Usecase - regrouper OpponentPawnsToPush et PawnsToMove dans un meme objet ?
  @PostMapping(value = "move", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PlayResultDto> move(@RequestBody MoveDto move) {
    try {
      if (game == null) {
        return ResponseEntity.badRequest().build();
      }
      PlayResult playResult = game.play(move.player(), move.toDomain());
      return ResponseEntity.ok(PlayResultDto.fromDomain(playResult));
    } catch (IllegalStateException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
