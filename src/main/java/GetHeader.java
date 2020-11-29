import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GetHeader {


  private String file;

  // Эту часть надо через getResourceAsStream
  private int nameLength = 128;
  private String name;

  private int commentLength = 2048;
  private String comment;

  private List<String> data = new ArrayList<String>();

  // Сюда пишем инструкции
  List<Instruction> instructions = new ArrayList<Instruction>();

  public GetHeader(byte[] buffer) {

    /*
     *** Считываем MAGIC HEADER (потом обернем в класс и выполним нужные проверки)
     */
    byte[] execMagic = new byte[4]; // execMagic
    System.arraycopy(buffer, 0, execMagic, 0, 4);
    int execMagicInt = ByteBuffer.wrap(execMagic).getInt();
    System.out.println("\nEXEC_MAGIC: " + String.format("%02X", execMagicInt));

    /*
     *** Считываем имя (потом обернем в класс и выполним проверки)
     */
    byte[] nameInBytes = new byte[nameLength]; // name
    int notNull = 0;
    while (nameInBytes[notNull] != 0) {
      notNull++;
    }
    System.arraycopy(buffer, 4, nameInBytes, 0, nameLength);
    while (nameInBytes[notNull] != 0) {
      notNull++;
    }
//    System.arraycopy(buffer, 4, nameInBytes, 0, notNull);
    this.name = new String(nameInBytes, 0, notNull);
    data.add(".name   \t" + this.name);
//    System.out.println("NAME: " + this.name);

    /*
     *** Получаем 4 null (потом обернуть в класс и выполнить проверки)
     */
    byte[] null_1 = new byte[40];
    System.arraycopy(buffer, nameLength + 4, null_1, 0, 4);
    int null_1_int = ByteBuffer.wrap(null_1, 0, 4).getInt();
    System.out.println("NULL_1: " + null_1_int);

    /*
     *** Получаем размер чемпиона (потом обернуть в класс и выполнить проверки)
     */
    byte[] champSizeInBytes = new byte[4];
    System.arraycopy(buffer, nameLength + 8, champSizeInBytes, 0, 4);
    int champSize = ByteBuffer.wrap(champSizeInBytes).getInt();
    System.out.println("CHAMP_SIZE: " + champSize);

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
    data.add(".comment\t" + this.comment);
//    System.out.println("COMMENT: " + this.comment);

    /*
     *** Получаем 4 null (потом обернуть в класс и выполнить проверки)
     */
    byte[] null_2 = new byte[4];
    System.arraycopy(buffer, nameLength + 12 + commentLength, null_2, 0, 4);
    int null_2_int = ByteBuffer.wrap(null_2, 0, 4).getInt();
    System.out.println("NULL_2: " + null_2_int);



    /*
     *** Записываем команды. Затем нужно провести сверку с общей длиной файла.
     *** Обернуть все в класс. Выполнить проверки.
     */
    byte[] commands = new byte[champSize];
    System.arraycopy(buffer, nameLength + 16 + commentLength, commands, 0, champSize);

    Instruction instruction = new Instruction(commands);

  }
}
