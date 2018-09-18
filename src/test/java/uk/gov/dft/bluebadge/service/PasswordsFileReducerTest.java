package uk.gov.dft.bluebadge.service;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class PasswordsFileReducerTest {

  @Test
  public void filter() throws IOException {
    List<String> top = readInFile();
    //		assertEquals(1000000, top.size());

    List<String> filtered = filterUnder8(top);
    assertTrue(filtered.size() < 1000000);

    Path path = saveOutFile(filtered);
    assertTrue(Files.isReadable(path));
  }

  private List<String> readInFile() throws IOException {
    String top1000000 =
        FileSystems.getDefault()
            .getPath("src/test/resources/top1000000")
            .normalize()
            .toAbsolutePath()
            .toString();

    Path path = Paths.get(top1000000);
    List<String> payload =
        Files.readAllLines(path).stream().map(s -> s.trim()).collect(Collectors.toList());

    return payload;
  }

  private List<String> filterUnder8(List<String> in) {
    List<String> out = in.stream().filter(w -> w.length() >= 8).collect(Collectors.toList());

    return out;
  }

  private Path saveOutFile(List<String> list) throws IOException {
    String top1000000filtered =
        FileSystems.getDefault()
            .getPath("src/test/resources/top1000000filtered")
            .normalize()
            .toAbsolutePath()
            .toString();

    Path path = Paths.get(top1000000filtered);
    Path file = Files.createFile(path);

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(top1000000filtered))) {

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
