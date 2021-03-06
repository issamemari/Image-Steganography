import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or

class LSB(numberOfBits: Int = 1, val numberOfChannels: Int = 3) : Steganographer {
    val numberOfBits: Int

    init {
        this.numberOfBits = numberOfBits
    }

    // Function takes as input a byte array, returns k bits in the array starting from a given offset in bits
    private fun getKBits(message: ByteArray, k: Int, offset: Int): Int {
        require(k in 0..8 && offset in 0..message.size * 8)

        val byteIndex = offset / 8
        val bitIndex = offset % 8

        if (byteIndex < message.size - 1) {
            val number = (message[byteIndex].toInt().shl(8)) or (message[byteIndex + 1].toInt() and 255)

            val thing1 = number shr (16 - (bitIndex + k))
            val thing2 = (1 shl k) - 1
            return (thing1) and (thing2)

        } else {
            val number = (message[byteIndex].toInt())
            val shiftRight = maxOf((8 - (bitIndex + k)), 0)
            val shiftLeft = minOf(8 - bitIndex, k)
            var res = (number shr shiftRight) and ((1 shl shiftLeft) - 1)
            if ((8 - bitIndex) < k)
                return res shl (k - (8 - bitIndex))
            return res
        }
    }

    // Function that actually writes the message to the cover image
    private fun fill(message: ByteArray, coverImage: Image): Image {

        // We avoid side effects (altering coverImage's content), by copying it first
        val result = coverImage.copy()


        /*
            We have to store how many LSB's our encoding is using, to do so
            We use the first pixel to store the information
            The valid values for LSB is [1..8]
            So to be able to store the number in binary in the first LSB we save values in [0..7]

         */
        val lsbPixel = result[0, 0]

        var lsb = numberOfBits - 1

        // It was for (channel in 0 until numberOfChannels), but I think it shouldn't be
        for (channel in 0 until 3) {
            lsbPixel[channel] = lsbPixel[channel] and 0xFE.toByte() or (lsb and 1).toByte()
            lsb = lsb shr 1
        }
        result[0, 0] = lsbPixel

        // Number of bits is constant always
        val k = numberOfBits
        var offset = 0

        //Flag used to check whether we finished encoding at any level of the 3 nested for-loops
        var hasFinished = false

        //We skip the first pixel as it contains how many LSB's are used
        for (i in 0 until result.width) {
            if (hasFinished) break

            for (j in 0 until result.height) {
                //We don't want to write over the LSB pixel
                if (i == 0 && j == 0) continue
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
        val numberOfUsableBitsInImage = numberOfBytesInImage * this.numberOfBits

        if (numberOfUsableBitsInImage < message.size * 8 + 3)
            throw RuntimeException("Message is too big to fit in the cover image")

        // Convert message size to a 4-byte array
        val buffer = ByteBuffer.allocate(4)
        buffer.putInt(message.size)
        val sizeByteArray = buffer.array()

        // Append message size byte array to the message
        val newMessage = sizeByteArray + message

        return fill(newMessage, coverImage)
    }

    override fun extract(coverImage: Image): ByteArray {
        var lsb = getLSB(coverImage[0, 0])

        val messageSize = ByteBuffer.wrap(readKByte(lsb, 0, 4, coverImage)).int
        val message = readKByte(lsb, 4 * 8, messageSize, coverImage)

        return message
    }

    fun getLSB(pixel: Pixel): Int {
        var result = 0

        for (i in 0 until numberOfChannels) {
            result = result + (pixel[i] and 1) * (Math.pow(2.0, i.toDouble())).toInt()
        }

        return result + 1
    }


    /*
        This functions reads k bytes starting from offset and uses lsb
        offset is in BITS
     */
    fun readKByte(lsb: Int, offset: Int, k: Int, image: Image): ByteArray {
        var counter = 0
        val numberOfBits = k * 8
        var seekPointer = 0
        var hasReachedOffset = false
        var hasFinished = false

        val byteBuffer = ByteBuffer.allocate(k)

        var byte: Byte = 0


        for (i in 0 until image.width) {
            if (hasFinished) break

            for (j in 0 until image.height) {
                if (hasFinished) break
                if (i == 0 && j == 0) continue
                if (!hasReachedOffset) {
                    if (seekPointer == offset) {
                        hasReachedOffset = true
                    }
                }
                for (channel in 0 until numberOfChannels) {
                    if (hasFinished) break

                    val data = image[i, j][channel]

                    for (bit in lsb - 1 downTo 0) {
                        if (hasFinished) break


                        if (!hasReachedOffset) {
                            seekPointer++
                            if (seekPointer == offset) {
                                hasReachedOffset = true
                            }
                        } else {
                            byte = Byte_set(byte, counter++ % 8, data[bit])
                            if (counter % 8 == 0 && counter > 0) {
                                byteBuffer.put(byte)
                                byte = 0
                            }

                            hasFinished = counter == numberOfBits
                        }
                    }

                }

            }
        }

        return byteBuffer.array()
    }


    operator fun Byte.get(position: Int): Boolean {
        return (this.toInt() shr position and 1) == 1
    }

    public fun Byte_set(byte: Byte, position: Int, value: Boolean): Byte {
        if (value)
            return (byte or ((1 shl (7 - position)).toByte()))
        else
            return byte and (1 shl (7 - position)).inv().toByte()

    }
}