import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class Disasm {
  // TODO типы проверок. Если размер меньше nameLength || commentLength || их суммы?
  // TODO добавить все проверки на тип чар для коммента или имени
  // TODO если magicNumber != прочитанному magicNumber
  // TODO если размер чемпиона не совпадает с прочитанным
  // TODO проверка имени файла и расширения
  // TODO дописать usage

  public static void main(String[] args) throws IOException {

    if (args[0] != null && checkFileName(args[0]) == 1) {
      String filePath = args[0];
      byte[] array = Files.readAllBytes(Paths.get(filePath));
      ParseFile parseFile = new ParseFile(array);
      String filename = createFilename(args[0]);
      writeFile(filename, parseFile.getParsedFile());


    } else {
      usage();
    }
  }

  private static int checkFileName(String filename) {
    String[] tmp = filename.split("\\.");
    if (!tmp[tmp.length - 1].equals("cor")) {
      System.out.println(tmp[tmp.length - 1]);
      return 0;
    }
    return 1;
  }

  private static String createFilename(String currentName) {
    return currentName.replaceAll("cor$", "s");
  }

  private static void writeFile(String filename, List<String> fileStrings) throws IOException {
    FileWriter writer = new FileWriter(filename);
    for(String str: fileStrings) {
      writer.write(str + System.lineSeparator());
    }
    writer.close();
  }

  private static void usage() {
    System.out.println("Usage: java -jar disasm-1.0-SNAPSHOT.jar \"path to file .cor\"");
  }
}
