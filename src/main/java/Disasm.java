import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class Disasm {
  // TODO типы проверок. Если размер меньше nameLength || commentLength || их суммы?
  // TODO если magicNumber != прочитанному magicNumber
  // TODO если размер чемпиона не совпадает с прочитанным
  // TODO проверка имени файла и расширения
  // TODO дописать usage

  public static void main(String[] args) throws IOException {

    int numBytesWaiting = System.in.available();
//    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//    String filePath = reader.readLine();
    String filePath = args[0];
    File file = new File(filePath);
    System.out.println("FILE_SIZE: " + numBytesWaiting);
//    byte[] fileInBytes = FileUtils.readFileToByteArray(file);
//    System.out.println("FROM FILE: " + fileInBytes.toString());
    byte[] array = Files.readAllBytes(Paths.get(filePath));


    // Read a buffer's worth of bytes at once
//    byte[] buffer = new byte[numBytesWaiting];
//    int offset = 0;
//    System.in.read(buffer, offset, buffer.length);
//
//    System.out.println("FROM INPUT: " + buffer.toString());
    GetHeader header = new GetHeader(array);

//    System.out.println("File bytes: " + new String(buffer));
  }
}
