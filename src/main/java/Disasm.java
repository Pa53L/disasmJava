import java.io.IOException;

public class Disasm {
  // TODO типы проверок. Если размер меньше nameLength || commentLength || их суммы?
  // TODO если magicNumber != прочитанному magicNumber
  // TODO если размер чемпиона не совпадает с прочитанным
  // TODO проверка имени файла и расширения

  public static void main(String[] args) throws IOException {
//    System.out.println("ARGS: " + args.length + " " + args[0]);
    // See approximately how many bytes are waiting to be read
    int numBytesWaiting = System.in.available();
    System.out.println("FILE_SIZE: " + numBytesWaiting);

    // Read a single byte (an int from 0-255)
//    for (int i = 0; i <numBytesWaiting; i++) {
//      if (i % 2 == 0 && i != 0) {
//        System.out.print(" ");
//      }
//      if (i % 16 == 0 && i != 0) {
//        System.out.println();
//      }
//      int singleByte = System.in.read();
//      String str = String.format("%02X", singleByte);
//      System.out.print(str);
//
//    }
//    int singleByte = System.in.read();
//    System.out.println("The first byte of standard input is: " + singleByte);

    // Read a buffer's worth of bytes at once
    byte[] buffer = new byte[numBytesWaiting];
    int offset = 0;
    System.in.read(buffer, offset, buffer.length);
    GetHeader header = new GetHeader(buffer);

//    System.out.println("File bytes: " + new String(buffer));
  }
}
