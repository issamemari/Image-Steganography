import java.awt.Color
import java.nio.ByteBuffer
import java.math.BigInteger
import kotlin.experimental.and
import kotlin.experimental.or


class LSB(numberOfBits: Int, val numberOfChannels: Int = 3) : Steganographer {
    val numberOfBits: Int

    init {
        this.numberOfBits = numberOfBits
    }

    // Function takes as input a byte array, returns k bits in the array starting from a given offset in bits
    // Must satisfy the following tests:
    //    TEST 1:
    //    val k = 5
    //    var offset = 36
    //
    //    val message: ByteArray = byteArrayOf(0b00001100, 0b00010101, 0b00100011, 0b00101100, 0b00111011)
    //    println(getKBits(message, k, offset))
    //    outputs 11
    //
    //    TEST 2:
    //    val k = 5
    //    var offset = 22
    //
    //    val message: ByteArray = byteArrayOf(0b00001100, 0b00010101, 0b00100011, 0b00101100, 0b00111011)
    //    println(getKBits(message, k, offset))
    //    outputs 25
    private fun getKBits(message: ByteArray, k: Int, offset: Int): Int {
        require(k in 0..8 && offset in 0..message.size * 8)

        val byteIndex = offset / 8
        val bitIndex = offset % 8

        if (byteIndex < message.size - 1) {
            val number = (message[byteIndex].toInt().shl(8)) or message[byteIndex + 1].toInt()
            return (number shr (16 - (bitIndex + k))) and ((1 shl k) - 1)
        } else {
            val number = (message[byteIndex].toInt())
            val shiftRight = maxOf((8 - (offset + k)), 0)
            val shiftLeft = minOf(k, 8 - offset)
            return (number shr shiftRight) and ((1 shl shiftLeft) - 1)
        }
    }

    // Function that actually writes the message to the cover image
    private fun fill(message: ByteArray, coverImage: Image): Image {

        // We avoid side effects (altering coverImage's content), by copying it first
        val result = coverImage.copy()


        // Number of bits is constant always
        val k = numberOfBits

        var offset = 0

        //Flag used to check whether we finished encoding at any level of the 3 nested for-loops
        var hasFinished = false

        for (i in 0 until result.width) {
            if (hasFinished) break

            for (j in 0 until result.height) {
                if (hasFinished) break

                // We extract the information stored in the ith,jth pixel in the original image
                val pixel = result[i, j]

                //For every channel in that pixel we store a chunk of the message

                for (channel in 0 until numberOfChannels) {
                    val chunk = getKBits(message, k, offset)
                    pixel[channel] = (pixel[channel] and (0xFF shl k).toByte() or chunk.toByte())
                    offset += k

                    //Check if we finished encoding
                    if (offset >= message.size * 8) {
                        hasFinished = true
                        break
                    }
                }

                //Write back the pixel loaded with the message
                result[i, j] = pixel
            }
        }


        return result
    }

    override fun embed(message: ByteArray, coverImage: Image): Image {
        // Check that message fits into image
        val numberOfBytesInImage = coverImage.width * coverImage.height * 3
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

    override fun extract(coverImage: Image): ByteArray {
        return ByteArray(5)
    }
}