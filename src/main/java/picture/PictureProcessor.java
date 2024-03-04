package picture;


//import static picture.ParserKt.parseInput;

import java.io.IOException;

import static picture.ParserKt.parseInput;


public class PictureProcessor {
  static void invert(String input, String output) {
    Picture pic = new Picture(input);
    Picture newPicture = new Picture(pic.getWidth(), pic.getHeight());
    for (int x = 0; x < pic.getWidth(); x++) {
      for (int y = 0; y < pic.getHeight(); y++) {
        Color currentColor = pic.getPixel(x, y);
        Color newColor = new Color(255 - currentColor.getRed(), 255 - currentColor.getGreen(), 255 - currentColor.getBlue());
        newPicture.setPixel(x, y, newColor);
      }
    }
    newPicture.save(output);
  }

  public static void main(String[] args) {
    // TODO: Implement this.
    parseInput(args);
//    invert(args[1], args[2]);
  }
}
