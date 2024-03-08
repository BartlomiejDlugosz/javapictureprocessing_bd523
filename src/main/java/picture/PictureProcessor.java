package picture;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PictureProcessor {
  public static void main(String[] args) {
    switch(args[0]) {
      case "invert":
        invert(args[1], args[2]);
        break;
      case "grayscale":
        grayscale(args[1], args[2]);
        break;
      case "rotate":
        rotate(Integer.valueOf(args[1]), args[2], args[3]);
        break;
      case "flip":
        flip(args[1].charAt(0), args[2], args[3]);
        break;
      case "blend":
        blend(Arrays.copyOfRange(args, 1, args.length - 1), args[args.length - 1]);
        break;
      case "blur":
        blur(args[1], args[2]);
        break;
      default:
//        throw new UnsupportedOperationException();
        break;
    }
  }

  private static void invert(String input, String output) {
    Picture picture = new Picture(input);
    picture.invert();
    picture.saveAs(output);
  }

  private static void grayscale(String input, String output) {
    Picture picture = new Picture(input);
    picture.grayscale();
    picture.saveAs(output);
  }

  private static void rotate(Integer angle, String input, String output) {
    Picture picture = new Picture(input);
    picture.rotate(angle, output);
//    picture.saveAs(output);
  }

  private static void flip(Character type, String input, String output) {
    Picture picture = new Picture(input);
    picture.flip(type, output);
  }

  private static void blend(String[] inputs, String output) {
    int minWidth = Integer.MAX_VALUE;
    int minHeight = Integer.MAX_VALUE;
    for (String input : inputs) {
      Picture pic = new Picture(input);
      if (pic.getWidth() < minWidth) {
        minWidth = pic.getWidth();
      }
      if (pic.getHeight() < minHeight)  {
        minHeight = pic.getHeight();
      }
    }
    Picture p = new Picture(minWidth, minHeight);
    p.blend(inputs, output);
    p.saveAs(output);
  }

  private static void blur(String input, String output) {
    Picture picture = new Picture(input);
    picture.blur(output);
  }

}
