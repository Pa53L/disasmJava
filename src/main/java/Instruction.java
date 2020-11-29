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
    long param = 0;
    if (type == 1) {
      param = commands[index];
      System.out.print("r" + param + " ");
      return 1;
    } else if (type == 3) {
      param = Integer.reverseBytes((int) param);
      System.out.print(":" + param + " ");
      return 2;
    } else if (type == 2) {
      return writeDir(type, op, commands);
    }
    return 0;
  }

  int writeDir(int type, byte op, byte[] commands) {
    long param = 0;

   if (type == 2 && Commands.bool[op] == 0) {
     param = commands[index];
     param = Integer.reverseBytes((int) param);
     System.out.print("%" + param + " ");
     return 4;
   } else if (type == 2 && Commands.bool[op] == 1) {
     param = commands[index];
     param = Integer.reverseBytes((int)param);
     System.out.print("%" + param + " ");
     return 2;
   }
    return 0;
  }

  int writeOther(byte op, byte[] commands) {
    short param;
    int secondParam = commands[index];

    if (op == 1) {
//      secondParam = Integer.reverseBytes(commands[index]);
      System.out.print("%" + secondParam + " ");
      return 4;
    } else if (op == 9 || op == 12 || op == 15) {
      param = commands[index];
      param = Short.reverseBytes(param);
      System.out.print("%" + param + " ");
      return 2;
    }
    return 0;
  }
}

//  int i = 0;
//    while (i < commands.length) {
//    int shift = writeCommand(commands, i);
//    System.out.println("i: " + i);
//    i += shift;
//    }
//
//    System.out.println("TOTAL READ: " + (nameLength + 16 + commentLength + champSize));
//    System.out.println("FILE SIZE: " + buffer.length);
//    }
//
//    int writeCommand(byte[] commands, int i) {
//    // i - file_data->index
//    int tmp = i;
//    int shift = 1; //enc
//    int index = commands[i]; //op
//
//    tmp += writeOperation(index, commands);
//    System.out.println("I after WOP: " + i);
//    if (Commands.getOctal(i - 1) == 1) {
//    shift = commands[i++];
//    if (((shift & 0b11000000) >> 6) != 0) {
//    System.out.println(" >>6");
//    tmp += writeParam((shift & 0b11000000) >> 6, index, commands);
//    }
//    if (((shift & 0b11000000) >> 4) != 0) {
//    System.out.println(" >>4");
//    tmp += writeParam((shift & 0b11000000) >> 6, index, commands);
//    }
//    if (((shift & 0b11000000) >> 2) != 0) {
//    System.out.println(" >>2");
//    tmp += writeParam((shift & 0b11000000) >> 6, index, commands);
//    }
//    }
//    else {
////      i += writeZjimpFork(index, commands);
//    if (index == 1) {
//    i += 4;
//    } else if (index == 10 || index == 3 || index == 16) {
//    i += 2;
//    } else tmp += 0;
//    }
//    System.out.println();
//
////    String command = "";
////    if (commands[i] > 0 && commands[i] < 17) {
////      command += Commands.getCommands(commands[i]) + "  ";
////    }
////    while (i < commands.length && (commands[i] <= 0 || commands[i] > 16)) {
////      System.out.println("ARGUMENTS ARE: " + commands[i] + "  " + i);
////      i++;
////      if (Commands.getOctal(i - 1) == 1) {
////        int encode = commands[i++];
////        if ((commands[i] & 0b11000000) >> 6 != 0) {
////          i +=
////        }
////      }
////      shift++;
////      i++;
////    }
////    System.out.println(command);
//    return tmp - i;
//    }
//
//    int writeParam(int type, int shift, byte[] commands) {
//    long param = 0;
//    if (type == 0b01) { //reg
//    System.out.print("r" + param + type + " ");
//    return 1;
//    } else if (type == 0b10) { // indir
//    param = (short) param << 0 | (short) param << 8;
//    System.out.println(":" + param + " ");
//    return 2;
//    } else if (type == 0b11) { // dir
//    return writeDir(type, shift, commands);
//    }
//    return 0;
//    }
//
//    int writeDir(int type, int shift, byte[] commands) {
//    long param = 0;
//    if (Commands.bool[shift] ==  0) {
//
//    return 4;
//    } else if (Commands.bool[shift] == 1) {
//    return 2;
//    }
//    return 0;
//    }
//
//    int writeOperation(int index, byte[] commands) {
//    if (index > 0 && index < 17) {
//    System.out.println("ARGUMENTS ARE: " + commands[index] + "  " + index);
//    }
//    return 1;
//    }
//
////  private static byte[] copyFirstFiveFieldsOfArrayUsingSystem(byte[] source) {
////    if(source.length > 5){
////      int[] temp=new int[5];
////      System.arraycopy(source, 0, temp, 0, 5);
////      return temp;
////    }else{
////      int[] temp=new int1;
////      System.arraycopy(source, 0, temp, 0, source.length);
////      return temp;
////    }
////
////  }