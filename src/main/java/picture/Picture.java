package picture;

import kotlin.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PipedReader;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

class Point {
  int x;
  int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}


/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /**
   * The internal image representation of this picture.
   */
  private final BufferedImage image;

  /**
   * Construct a new (blank) Picture object with the specified width and height.
   */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Construct a new Picture from the image data in the specified file.
   */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   * otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
        || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x   the x-coordinate of the pixel to be updated
   * @param y   the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
        x,
        y,
        0xff000000
            | (((0xff & rgb.getRed()) << 16)
            | ((0xff & rgb.getGreen()) << 8)
            | (0xff & rgb.getBlue())));
  }

  /**
   * Returns a String representation of the RGB components of the picture.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  public void invert() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        Color currentColor = getPixel(x, y);
        Color newColor = new Color(255 - currentColor.getRed(), 255 - currentColor.getGreen(), 255 - currentColor.getBlue());
        setPixel(x, y, newColor);
      }
    }
  }

  public void grayscale() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        Color currentColor = getPixel(x, y);
        int avg = (currentColor.getRed() + currentColor.getGreen() + currentColor.getBlue()) / 3;
        Color newColor = new Color(avg, avg, avg);
        setPixel(x, y, newColor);
      }
    }
  }

  public void rotate(Integer ang, String output) {
    double angle = Math.toRadians(ang);
    int newWidth = (int) Math.round(Math.abs(getWidth() * cos(angle)) + Math.abs(getHeight() * sin(angle)));
    int newHeight = (int) Math.round(Math.abs(getWidth() * sin(angle)) + Math.abs(getHeight() * cos(angle)));
    Picture newPicture = new Picture(newWidth, newHeight);

    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        double translatedX = x - ((double) getWidth() / 2);
        double translatedY = (-y) + ((double) getHeight() / 2);
        double rotatedX = (translatedX * cos(-angle)) - (translatedY * sin(-angle));
        double rotatedY = (translatedX * sin(-angle)) + (translatedY * cos(-angle));
        double newX = rotatedX + ((double) newWidth / 2);
        double newY = -(rotatedY - ((double) newHeight / 2));
//        System.out.println(newX + " " + Math.round(newX) + " " + Math.max(Math.min(Math.round(newX), newWidth - 1), 0) + " " + newY);
//        newPoints.put(new Point(newX, newY), getPixel(x, y));
        switch(ang) {
          case 90:
            newX -= 1;
            break;
          case 270:
            newY -= 1;
            break;
          case 180:
            newX -=1;
            newY -= 1;
            break;
        }
        newPicture.setPixel((int) Math.max(Math.min(Math.round(newX), newWidth - 1), 0), (int) Math.max(Math.min(Math.round(newY), newHeight - 1), 0), getPixel(x, y));
      }
    }
    newPicture.saveAs(output);
  }

  public void flip(Character type) {
  }

  public void blur() {
  }
}
