import java.nio.ByteBuffer;

public class Instruction {

  static int index = 0;
  int shift;

  public Instruction(byte[] commands) {
    while (index < commands.length) {
      writeCommand(commands);
      System.out.println();
    }

  }

  private void writeCommand(byte[] commands) {
    byte op;
    byte enc;

    op = (byte) (commands[index] & 0xff);;
    index += writeOperation(op, commands);
    if (Commands.getOctal(op) == 1) {
      enc = (byte) (commands[index]  & 0xff);
      index += 1;
      if ((((enc & 0b11000000) >> 6) & 0xff) != 0) {
        index += writeParam((((enc & 0b11000000) & 0xff) >> 6), op, commands);
      }
      if ((((enc & 0b00110000) >> 4) & 0xff) != 0) {
        index += writeParam((((enc & 0b00110000) >> 4) & 0xff), op, commands);
      }
      if ((((enc & 0b00001100) >> 2) & 0xff) != 0) {
        index += writeParam((((enc & 0b00001100) >> 2) & 0xff), op, commands);
      }
    } else {
      index += writeOther(op, commands);

    }
  }

  private int writeOperation(byte op, byte[] commands) {
    if (op > 0 && op < 17) {
      System.out.print(Commands.getCommands(op) + "  ");
    }
    return 1;
  }

  private int writeParam(int type, byte op, byte[] commands) {
    int param = 0;
    if (type == 1) {
      param = commands[index];
      System.out.print("r" + param + " ");
      return 1;
    } else if (type == 3) {
      param = Integer.reverseBytes(param);
      System.out.print(":" + param + " ");
      return 2;
    } else if (type == 2) {
      return writeDir(type, op, commands);
    }
    return 0;
  }

  int writeDir(int type, byte op, byte[] commands) {
    int param = 0;
    short otherParam = 0;

    byte p1;
    byte p2;

    byte p3;
    byte p4;

   if (type == 2 && Commands.bool[op] == 0) {
     p1 = commands[index];
     p2 = commands[index + 1];
     p3 = commands[index + 2];
     p4 = commands[index + 3];
     byte[] params = { p1, p2, p3, p4 };
     ByteBuffer wrapped = ByteBuffer.wrap(params); // big-endian by default
     param = wrapped.getInt();

//     param = commands[index];
//     param = Integer.reverseBytes(param);
     System.out.print("%" + param + " ");
     return 4;
   } else if (type == 2 && Commands.bool[op] == 1) {
     p1 = commands[index];
     p2 = commands[index + 1];
     byte[] arr = { p1, p2 };
     ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
     otherParam = wrapped.getShort();

//     param = commands[index];
//     param = Integer.reverseBytes(param);
     System.out.print("%" + otherParam + " ");
     return 2;
   }
    return 0;
  }

  int writeOther(byte op, byte[] commands) {

    byte p1;
    byte p2;

    byte p3;
    byte p4;

    short param;
    int secondParam = commands[index];

    if (op == 1) {
      p1 = commands[index];
      p2 = commands[index + 1];
      p3 = commands[index + 2];
      p4 = commands[index + 3];
      byte[] params = { p1, p2, p3, p4 };
      ByteBuffer wrapped = ByteBuffer.wrap(params); // big-endian by default
      secondParam = wrapped.getInt();
      System.out.print("%" + secondParam + " ");
      return 4;
    } else if (op == 9 || op == 12 || op == 15) {
      p1 = commands[index];
      p2 = commands[index + 1];
      byte[] arr = { p1, p2 };
      ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
      param = wrapped.getShort();
      System.out.print("%" + param + " ");
      return 2;
    }
    return 0;
  }
}
