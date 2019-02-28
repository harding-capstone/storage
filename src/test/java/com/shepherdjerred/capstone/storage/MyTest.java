package com.shepherdjerred.capstone.storage;

import com.shepherdjerred.capstone.logic.board.Board;
import com.shepherdjerred.capstone.logic.board.BoardPieces;
import com.shepherdjerred.capstone.logic.board.BoardPiecesInitializer;
import com.shepherdjerred.capstone.logic.board.BoardSettings;
import com.shepherdjerred.capstone.logic.board.layout.BoardCellsInitializer;
import com.shepherdjerred.capstone.logic.board.layout.BoardLayout;
import com.shepherdjerred.capstone.logic.match.Match;
import com.shepherdjerred.capstone.logic.match.MatchSettings;
import com.shepherdjerred.capstone.logic.match.MatchSettings.PlayerCount;
import com.shepherdjerred.capstone.logic.player.PlayerId;
import com.shepherdjerred.capstone.storage.save.FilesystemSavedGameRepository;
import com.shepherdjerred.capstone.storage.save.SavedGameFile;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MyTest {

  @Test
  public void testSaves() throws IOException {
    var currentRelativePath = Paths.get("").toAbsolutePath();

    System.out.println(currentRelativePath.toString());

    var repository = new FilesystemSavedGameRepository(currentRelativePath);
    var match = createMatch();

    repository.saveMatch("My match.match", match);

    var savedGames = repository.getSaves();
    System.out.println(savedGames);

    var savedGame = new SavedGameFile("My match.match", Instant.now(), currentRelativePath);
    var loadedGame = repository.loadMatch(savedGame);

    System.out.println(match.equals(loadedGame.get()));
  }

  private Match createMatch() {
    var boardSettings = new BoardSettings(9, PlayerCount.TWO);
    var matchSettings = new MatchSettings(10, PlayerId.ONE, boardSettings);

    var boardLayout = BoardLayout.fromBoardSettings(new BoardCellsInitializer(), boardSettings);
    var boardPieces = BoardPieces.initializePieceLocations(boardSettings,
        new BoardPiecesInitializer());
    var board = Board.createBoard(boardLayout, boardPieces);
    var match = Match.startNewMatch(matchSettings, board);
    return match;
  }
}
