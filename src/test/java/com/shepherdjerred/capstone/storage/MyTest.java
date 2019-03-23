package com.shepherdjerred.capstone.storage;

import com.shepherdjerred.capstone.logic.board.BoardSettings;
import com.shepherdjerred.capstone.logic.match.Match;
import com.shepherdjerred.capstone.logic.match.MatchSettings;
import com.shepherdjerred.capstone.logic.match.serialization.MatchJsonSerializer;
import com.shepherdjerred.capstone.logic.player.PlayerCount;
import com.shepherdjerred.capstone.logic.player.QuoridorPlayer;
import com.shepherdjerred.capstone.storage.save.FilesystemSavedGameRepository;
import com.shepherdjerred.capstone.storage.save.SavedGameFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import org.junit.Assert;
import org.junit.Test;

public class MyTest {

  private String fileName = "My match.match";

  @Test
  public void testSaves() throws IOException {
    var currentRelativePath = Paths.get("").toAbsolutePath();

    System.out.println(currentRelativePath.toString());

    var repository = new FilesystemSavedGameRepository(currentRelativePath);
    var match = Match.from(new MatchSettings(10, QuoridorPlayer.ONE, PlayerCount.TWO),
        new BoardSettings(9, PlayerCount.TWO));

    repository.saveMatch(fileName, match);

    var savedGames = repository.getSaves();
    System.out.println(savedGames);

    var savedGame = new SavedGameFile(fileName, Instant.now(), currentRelativePath);
    var loadedGame = repository.loadMatch(savedGame);

    Assert.assertTrue(loadedGame.isPresent());
    Assert.assertTrue(compareMatch(match, loadedGame.get()));

    Files.deleteIfExists(Paths.get(fileName));
  }

  private boolean compareMatch(Match match1, Match match2) {
    String match1String = new MatchJsonSerializer().toJsonString(match1);
    String match2String = new MatchJsonSerializer().toJsonString(match2);

    return match1String.equals(match2String);
  }
}
