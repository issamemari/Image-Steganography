import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun readImage(imagePath: String) : Array<Array<Color>>
{
    val imageFile = File(imagePath)

    if (!imageFile.exists()) throw RuntimeException("File $imagePath not found.")
    if (imageFile.isDirectory) throw RuntimeException("$imagePath is a directory, not an image.")

    var image: BufferedImage = ImageIO.read(imageFile)

    val width = image.width
    val height = image.height

    val colors2d = Array<Array<Color>>(height) {
        Array<Color>(width) {
            Color(0,0, 0)
        }
    }

    for (i in 0 .. height - 1)
        for (j in 0 .. width - 1)
            colors2d[i][j] = Color(image.getRGB(j, i))

    return colors2d
}