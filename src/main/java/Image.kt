import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO

class Image(val imgPath: String) {

    val image: BufferedImage
    val height: Int
    val width: Int

    init {
        val imageFile = File(imgPath)

        if (!imageFile.exists()) throw FileNotFoundException("File $imgPath not found.")
        if (imageFile.isDirectory) throw IllegalArgumentException("$imgPath is a directory, not an image.")

        image = ImageIO.read(imageFile)

        width = image.width
        height = image.height
    }


    operator fun get(i: Int, j: Int): Pixel {
        require(i in 0..width && j in 0..height)
        return Pixel(image.getRGB(i, j))
    }

    operator fun set(i: Int, j: Int, p: Pixel): Unit {
        require(i in 0..width && j in 0..height)
        image.setRGB(i, j, p.getColorCode())
    }

    fun exportImage(imagePath: String = "image", imageType: String = "jpg") {


        val outputfile = File("$imagePath.$imageType")
        ImageIO.write(image, imageType, outputfile)

    }

}

class Pixel(colorCode: Int) {

    private var alpha: Byte
    private var r: Byte;
    private var g: Byte;
    private var b: Byte


    init {
        b = (colorCode and 0xff).toByte()
        g = (colorCode and 0xff00 shr 8).toByte()
        r = (colorCode and 0xff0000 shr 16).toByte()

        alpha = (colorCode and 0xff000000.toInt() ushr 32).toByte()
    }


    var Alpha: Byte
        get() = alpha
        set(value: Byte) {
            require(value.isByte())
            alpha = value
        }

    var R: Byte
        get() = r
        set(value: Byte) {
            require(value.isByte())
            r = value
        }


    var G: Byte
        get() = g
        set(value: Byte) {
            require(value.isByte())
            g = value
        }


    var B: Byte
        get() = b
        set(value: Byte) {
            require(value.isByte())
            b = value
        }


    fun Byte.isByte() = this in Byte.MIN_VALUE..Byte.MAX_VALUE

    fun getColorCode(): Int {
        val hex = String.format("%02x%02x%02x%02x", alpha, r, g, b)
        return hex.toLong(16).toInt()
    }
}