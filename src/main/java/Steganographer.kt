import java.awt.Color

interface Steganographer {
    fun embed(message: ByteArray, coverImage: Image): Image
    fun extract(coverImage: Image): ByteArray
}