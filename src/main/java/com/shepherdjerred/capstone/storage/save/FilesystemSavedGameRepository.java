package com.shepherdjerred.capstone.storage.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shepherdjerred.capstone.logic.board.BoardPieces;
import com.shepherdjerred.capstone.logic.match.Match;
import com.shepherdjerred.capstone.logic.serialization.BoardPiecesDeserializer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesystemSavedGameRepository implements SavedGameRepository {

  private static Gson gson;

  static {
    gson = new GsonBuilder()
        .registerTypeAdapter(BoardPieces.class, new BoardPiecesDeserializer())
        .create();
  }

  private final Path directory;

  public FilesystemSavedGameRepository(Path directory) {
    this.directory = directory;
  }

  @Override
  public Set<SavedGame> getSaves() throws IOException {
    try (Stream<Path> files = Files.walk(directory)) {
      return files
          .filter(Files::isRegularFile)
          .filter(this::hasExtension)
          .map(this::getSavedGameFromPath)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .collect(Collectors.toSet());
    }
  }

  private Optional<SavedGameFile> getSavedGameFromPath(Path path) {
    var savedGame = new SavedGameFile(path.getFileName().toString(), Instant.now(), path);
    return Optional.of(savedGame);
  }

  private boolean hasExtension(Path path) {
    return path.getFileName().toString().endsWith(".match");
  }

  @Override
  public Optional<Match> loadMatch(SavedGame savedGame) throws IOException {
    try (var reader = new FileReader(savedGame.getName())) {
      var match = gson.fromJson(reader, Match.class);
      if (match != null) {
        return Optional.of(match);
      }
    }
    return Optional.empty();
  }

  @Override
  public void saveMatch(String name, Match match) throws IOException {
    try (var writer = new FileWriter(name)) {
      gson.toJson(match, writer);
    }
  }
}
