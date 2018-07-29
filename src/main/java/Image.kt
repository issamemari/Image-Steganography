import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO


/*
    You can either instantiate an Image by providing a BufferedImage or by providing its path
    NOTE:
        AT LEAST one option should be used.
        Using a BufferedImage takes precedence over providing a file path.
 */

class Image(imgPath: String = "", imageBuffer: BufferedImage? = null) {

    private val image: BufferedImage
    val height: Int
    val width: Int

    init {
        image = if (imageBuffer != null) {
            imageBuffer
        } else {
            val imageFile = File(imgPath)

            if (!imageFile.exists()) throw FileNotFoundException("File $imgPath not found.")
            if (imageFile.isDirectory) throw IllegalArgumentException("$imgPath is a directory, not an image.")

            ImageIO.read(imageFile)
        }

        width = image.width
        height = image.height
    }


    operator fun get(i: Int, j: Int): Pixel {
        require(i in 0 until width && j in 0 until height)
        return Pixel(image.getRGB(i, j))
    }

    operator fun set(i: Int, j: Int, p: Pixel): Unit {
        require(i in 0 until width && j in 0 until height)
        image.setRGB(i, j, p.getColorCode())
    }

    fun exportImage(imagePath: String = "image", imageType: String = "jpg") {


        val outputfile = File("$imagePath.$imageType")
        ImageIO.write(image, imageType, outputfile)

    }

    fun copy() = Image(imageBuffer = image)

}

class Pixel(colorCode: Int, private val numberOfChannels: Int = 3) {

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


    operator fun set(index: Int, value: Byte) {
        require(index in 0 until numberOfChannels)
        require(value.isByte())

        when (index) {
            0 -> R = value
            1 -> G = value
            2 -> B = value
            3 -> Alpha = value
        }
    }

    operator fun get(index: Int): Byte {
        require(index in 0 until numberOfChannels)

        when (index) {
            0 -> return R
            1 -> return G
            2 -> return B
            3 -> return Alpha
        }

        throw IllegalArgumentException()
    }

    fun Byte.isByte() = this in Byte.MIN_VALUE..Byte.MAX_VALUE

    fun getColorCode(): Int {
        val hex = String.format("%02x%02x%02x%02x", alpha, r, g, b)
        return hex.toLong(16).toInt()
    }
}