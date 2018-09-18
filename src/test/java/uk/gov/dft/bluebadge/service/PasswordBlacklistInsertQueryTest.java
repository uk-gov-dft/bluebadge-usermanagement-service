package uk.gov.dft.bluebadge.service;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class PasswordBlacklistInsertQueryTest {

  //	INSERT INTO local_authority_area (id)
  //	VALUES ('England'), ('Wales'), ('Scotland'), ('Northern Ireland');

  private static final String INSERT = "INSERT INTO passwords_blacklist (id) VALUES ";

  @Test
  public void queries() throws IOException {
    List<String> queries = createQueries();
    //		assertEquals(1000000, top.size());

    Path pathQueriees = saveOutFile(queries, "src/test/resources/insert_queries.sql");
    assertTrue(Files.isReadable(pathQueriees));

    List<String> quaranteen = quaranteen();
    Path pathQuaranteen = saveOutFile(quaranteen, "src/test/resources/quaranteen.txt");
    assertTrue(Files.isReadable(pathQuaranteen));
  }

  private List<String> createQueries() throws IOException {
    String top1000000 =
        FileSystems.getDefault()
            .getPath("src/test/resources/top1000000filtered")
            .normalize()
            .toAbsolutePath()
            .toString();

    Path path = Paths.get(top1000000);
    List<String> payload =
        Files.readAllLines(path).stream().map(s -> s.trim()).collect(Collectors.toList());

    int total = payload.size();
    int chunk = 5000;

    List<String> queries = new ArrayList<>();
    for (int i = 0; i < total / chunk + 1; i++) {
      String part =
          payload
              .stream()
              .skip(i * chunk)
              .limit(chunk)
              .filter(word -> !word.contains(";"))
              .filter(word -> !word.contains("'"))
              .map(word -> "('" + word + "')," + System.lineSeparator())
              .collect(Collectors.joining());
      String query = INSERT + System.lineSeparator() + part.replaceFirst(".$", ";");
      queries.add(query);
      queries.add(System.lineSeparator());
    }

    return queries;
  }

  private List<String> quaranteen() throws IOException {
    String top1000000 =
        FileSystems.getDefault()
            .getPath("src/test/resources/top1000000filtered")
            .normalize()
            .toAbsolutePath()
            .toString();

    Path path = Paths.get(top1000000);
    List<String> payload =
        Files.readAllLines(path).stream().map(s -> s.trim()).collect(Collectors.toList());

    List<String> semicolon =
        payload.stream().filter(w -> w.contains(";")).collect(Collectors.toList());
    List<String> quote = payload.stream().filter(w -> w.contains("'")).collect(Collectors.toList());

    boolean success = semicolon.addAll(quote);

    return semicolon;
  }

  private Path saveOutFile(List<String> list, String filepath) throws IOException {
    String insertQueries =
        FileSystems.getDefault().getPath(filepath).normalize().toAbsolutePath().toString();

    Path path = Paths.get(insertQueries);
    Path file = Files.createFile(path);

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(insertQueries, false))) {

      for (String word : list) {
        bw.write(word);
        bw.newLine();
      }
    } catch (IOException e) {

      e.printStackTrace();
    }

    return file;
  }
}
