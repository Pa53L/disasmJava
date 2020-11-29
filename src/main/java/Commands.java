public class Commands {

  static String[] commands = { "empty string",
      "live",
      "ld",
      "st",
      "add",
      "sub",
      "and",
      "or",
      "xor",
      "zjmp",
      "ldi",
      "sti",
      "fork",
      "lld",
      "lldi",
      "lfork",
      "aff"
  };

  static int[] octal = { 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 };
  static int[] bool = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0 };
  static int[] argsLen = { 0, 1, 2, 2, 3, 3, 3, 3, 3, 1, 3, 3, 1, 2, 3, 1, 1 };

  public static String getCommands(int i) {
    return commands[i];
  }
  public static int getOctal(int i) { return octal[i]; }
  public static int getArgsLen(int i) { return argsLen[i]; }
}
