import java.io.FileReader;
import java.util.Properties;

public class ParseFile {

  private int nameLength;
  private int commentLength;

  public ParseFile() {
    try (FileReader reader = new FileReader("./classes/config")){
      Properties properties = new Properties();
      properties.load(reader);
      this.nameLength = Integer.parseInt(properties.getProperty("name.length"));
      this.commentLength = Integer.parseInt(properties.getProperty("comment.length"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
