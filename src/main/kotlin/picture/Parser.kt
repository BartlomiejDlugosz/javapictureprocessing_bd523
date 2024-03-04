package picture

import javax.imageio.ImageIO

fun invert(input: String, output: String) {
  val pic = Picture(input)
  val newPicture = Picture(pic.width, pic.height)
  for (x in 0..<pic.width) {
    for (y in 0..<pic.height) {
      val currentColor = pic.getPixel(x, y)
      val newColor = Color(255 - currentColor.red, 255 - currentColor.green, 255 - currentColor.blue)
      newPicture.setPixel(x, y, newColor)
    }
  }
  newPicture.save(output)
}

fun grayscale(input: String, output: String) {

}

fun rotate(angle: Int, input: String, output: String) {

}

fun flip(type: Char, input: String, output: String) {

}

fun blend(inputs: List<String>, output: String) {

}

fun blur(input: String, output: String) {

}

fun parseInput(inputs: Array<String>) {
  if (inputs[0] == "invert") invert(inputs[1], inputs[2])
  else if (inputs[0] == "grayscale") grayscale(inputs[1], inputs[2])
  else if (inputs[0] == "rotate") rotate(inputs[1].toInt(), inputs[2], inputs[3])
  else if (inputs[0] == "flip") flip(inputs[1].first(), inputs[2], inputs[3])
  else if (inputs[0] == "blend") blend(inputs.slice(1..(inputs.size - 2)), inputs[inputs.size - 1])
  else blur(inputs[0], inputs[1])
}

fun main() {
}