import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instruction {

  List<String> instructions = new ArrayList<>();
  String instruction = "";



  static int index = 0;

  public Instruction(byte[] commands) {
    while (index < commands.length) {
      writeCommand(commands);
//      System.out.println();
      instruction = instruction.trim();
      String[] arr = instruction.split(" ");
      for (int i = 0; i < arr.length; i++) {
        if (i != 0 && i < arr.length - 1) {
          arr[i] += ",";
        }
      }
      instruction = String.join(" ", arr);
      instructions.add(instruction);
      instruction = "";
    }
    for (int i = 0; i < instructions.size(); i ++) {
      System.out.println(instructions.get(i));
    }
  }

  public List<String> getInstructions() {
    return instructions;
  }

  private void writeCommand(byte[] commands) {
    byte op;
    byte enc;

    op = (byte) (commands[index] & 0xff);
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
//      System.out.print(Commands.getCommands(op) + "  ");
      instruction += Commands.getCommands(op) + " ";
    }
    return 1;
  }

  private int writeParam(int type, byte op, byte[] commands) {
    int param = 0;
    if (type == 1) {
      param = commands[index];
//      System.out.print("r" + param + " ");
      instruction += "r" + param + " ";
      return 1;
    } else if (type == 3) {
      param = Integer.reverseBytes(param);
//      System.out.print(":" + param + " ");
      instruction += ":" + param + " ";
      return 2;
    } else if (type == 2) {
      return writeDir(type, op, commands);
    }
    return 0;
  }

  int writeDir(int type, byte op, byte[] commands) {
    int param;
    short otherParam;

   if (type == 2 && Commands.bool[op] == 0) {
     param = getInt(commands, index);
//     System.out.print("%" + param + " ");
     instruction += "%" + param + " ";
     return 4;
   } else if (type == 2 && Commands.bool[op] == 1) {
     otherParam = getShort(commands, index);
//     System.out.print("%" + otherParam + " ");
     instruction += "%" + otherParam + " ";
     return 2;
   }
    return 0;
  }

  int writeOther(byte op, byte[] commands) {

    short param;
    int secondParam;

    if (op == 1) {
      secondParam = getInt(commands, index);
//      System.out.print("%" + secondParam + " ");
      instruction += "%" + secondParam + " ";
      return 4;
    } else if (op == 9 || op == 12 || op == 15) {
      param = getShort(commands, index);
//      System.out.print("%" + param + " ");
      instruction += "%" + param + " ";
      return 2;
    }
    return 0;
  }

  private short getShort(byte[] commands, int index) {
    byte b1 = commands[index];
    byte b2 = commands[index + 1];
    byte[] arr = { b1, b2 };
    return ByteBuffer.wrap(arr).getShort(); // big-endian by default
  }

  private int getInt(byte[] commands, int index) {
    byte b1 = commands[index];
    byte b2 = commands[index + 1];
    byte b3 = commands[index + 2];
    byte b4 = commands[index + 3];
    byte[] arr = { b1, b2, b3, b4 };
    return ByteBuffer.wrap(arr).getInt(); // big-endian by default
  }
}
