import java.awt.Color
import java.nio.ByteBuffer
import java.math.BigInteger



class LSB(numberOfBits: Int): Steganographer
{
    val numberOfBits: Int

    init {
        this.numberOfBits = numberOfBits
    }

    // Function that takes as input a byte, returns the offsetth k bits
    // Example:
    // input:
    //      number = 100 (01100100 in binary)
    //      k = 2
    //      offset = 2
    // output:
    //      2 (10 in binary)
    private fun getKBits(number: BigInteger, k: Int, offset: Int): Int
    {
        return (number shr (8 - (offset * k))).and(BigInteger.valueOf(((1.shl(k)) - 1).toLong())).toInt()
    }

    // Function that actually writes the message to the cover image
    private fun fill(message: ByteArray, coverImage: Array<Array<Color>>): Array<Array<Color>>
    {
        // For now it returns an empty array
        return Array<Array<Color>>(5) {
            Array<Color>(5) {
                Color(0, 0, 0)
            }
        }
    }

    override fun embed(message: ByteArray, coverImage: Array<Array<Color>>): Array<Array<Color>>
    {
        // Check that message fits into image
        val numberOfBytesInImage = coverImage.size * coverImage[0].size * 3
        val numberOfUsableBytesInImage = numberOfBytesInImage * this.numberOfBits / 8
        if (numberOfUsableBytesInImage < message.size) throw RuntimeException("Message is too big to fit in the cover image")

        // Convert message size to a 4-byte array
        val buffer = ByteBuffer.allocate(4)
        buffer.putInt(message.size)
        val sizeByteArray = buffer.array()

        // Append message size byte array to the message
        val newMessage = sizeByteArray + message

        return fill(newMessage, coverImage)
    }

    override fun extract(coverImage: Array<Array<Color>>): ByteArray
    {
        return ByteArray(5)
    }
}