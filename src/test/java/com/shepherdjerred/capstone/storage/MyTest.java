package com.shepherdjerred.capstone.storage;

import com.shepherdjerred.capstone.logic.board.BoardSettings;
import com.shepherdjerred.capstone.logic.match.Match;
import com.shepherdjerred.capstone.logic.match.MatchSettings;
import com.shepherdjerred.capstone.logic.player.PlayerCount;
import com.shepherdjerred.capstone.logic.player.QuoridorPlayer;
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
    var match = Match.from(new MatchSettings(10, QuoridorPlayer.ONE, PlayerCount.TWO),
        new BoardSettings(9, PlayerCount.TWO));

    repository.saveMatch("My match.match", match);

    var savedGames = repository.getSaves();
    System.out.println(savedGames);

    var savedGame = new SavedGameFile("My match.match", Instant.now(), currentRelativePath);
    var loadedGame = repository.loadMatch(savedGame);

    System.out.println(match.equals(loadedGame.get()));
  }
}
