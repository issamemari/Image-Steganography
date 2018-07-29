import java.awt.Color

interface Steganographer
{
    fun embed(message: ByteArray, coverImage: Array<Array<Color>>): Array<Array<Color>>
    fun extract(coverImage: Array<Array<Color>>): ByteArray
}