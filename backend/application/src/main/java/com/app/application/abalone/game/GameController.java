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
@RequestMapping("/api/auth/")
public class GameController {

  private Game game;

  @GetMapping(value = "new", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GameDto> newGame() {
    game = new Game();
    return ResponseEntity.ok(GameDto.fromGame(game));
  }

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
