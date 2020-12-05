import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParseFile {

  private int nameLength;
  private int commentLength;
  private int magicExec;

  List<String> parsedFile = new ArrayList<>();

  String name;
  String comment;

  public ParseFile(byte[] buffer) {
    try (FileReader reader = new FileReader("./classes/config")){
      Properties properties = new Properties();
      properties.load(reader);
      this.nameLength = Integer.parseInt(properties.getProperty("name.length"));
      this.commentLength = Integer.parseInt(properties.getProperty("comment.length"));
      this.magicExec = Integer.parseInt(properties.getProperty("magic.exec"));
      if (buffer.length < (nameLength + commentLength + 20)) {
        System.out.println("File is too short.");
        System.exit(0);
      }
      /*
      *** Reading and checking exec
       */
      int execFromFile = getExecFromFile(buffer);
      checkExec(execFromFile);
      /*
      *** Reading champion's name
       */
      byte[] nameInBytes = new byte[this.nameLength]; // name
      int notNull = 0;
      System.arraycopy(buffer, 4, nameInBytes, 0, this.nameLength);
      while (nameInBytes[notNull] != 0) {
        notNull++;
      }
      this.name = new String(nameInBytes, 0, notNull);
      checkNameLength(notNull);
      parsedFile.add(".name   \t\"" + this.name + "\"");
      /*
      *** Получаем 4 null, должны дать 0 в итоге
       */
      byte[] firstNull = new byte[40];
      System.arraycopy(buffer, nameLength + 4, firstNull, 0, 4);
      int firstNullInt = ByteBuffer.wrap(firstNull, 0, 4).getInt();
      checkNull(firstNullInt);
      /*
      *** Получаем размер чемпиона
       */
      byte[] champSizeInBytes = new byte[4];
      System.arraycopy(buffer, nameLength + 8, champSizeInBytes, 0, 4);
      int champSize = ByteBuffer.wrap(champSizeInBytes).getInt();
      /*
      *** Получаем комментарий (потом обернем в класс и выполним проверки)
       */
      byte[] commentInBytes = new byte[commentLength];
      System.arraycopy(buffer, nameLength + 12, commentInBytes, 0, commentLength);
      int notNull2 = 0;
      while (commentInBytes[notNull2] != 0) {
        notNull2++;
      }
      this.comment = new String(commentInBytes, 0, notNull2);
      parsedFile.add(".comment\t\"" + this.comment + "\"");
      /*
      *** Получаем 4 null (потом обернуть в класс и выполнить проверки)
       */
      byte[] secondNull = new byte[4];
      System.arraycopy(buffer, nameLength + 12 + commentLength, secondNull, 0, 4);
      int secondNullInt = ByteBuffer.wrap(secondNull, 0, 4).getInt();
      checkNull(secondNullInt);
      /*
      *** Записываем команды. Затем нужно провести сверку с общей длиной файла.
       */
      byte[] commands = new byte[champSize];
      System.arraycopy(buffer, nameLength + 16 + commentLength, commands, 0, champSize);

      Instruction instruction = new Instruction(commands);
      if (Instruction.index != champSize) {
        System.out.println("Incorrect champion's size.");
        System.exit(0);
      }
      parsedFile.addAll(instruction.getInstructions());
      for (String s : parsedFile) {
        System.out.println(s);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private int getExecFromFile(byte[] buffer) {
    byte[] execMagic = new byte[4]; // execMagic
    System.arraycopy(buffer, 0, execMagic, 0, 4);
    return ByteBuffer.wrap(execMagic).getInt();
  }

  private void checkExec(int execFromFile) {
    if (execFromFile != this.magicExec) {
      System.out.println("FROM F: " + execFromFile + "OPTION: " + this.magicExec);
      System.out.println("Incorrect file 1.");
      System.exit(0);
    }
  }

  private void checkNameLength(int check) {
    if (check > this.nameLength) {
      System.out.println("Champion's name is too long.");
      System.exit(0);
    }
  }

  private void checkNull(int nullInt) {
    if (nullInt != 0) {
      System.out.println("Incorrect file 2.");
      System.exit(0);
    }
  }

  public List<String> getParsedFile() {
    return parsedFile;
  }

}
