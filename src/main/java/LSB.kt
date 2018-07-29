import java.awt.Color
import java.nio.ByteBuffer
import java.math.BigInteger
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import com.sun.xml.internal.bind.v2.model.core.ID


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
            val shiftRight = maxOf((8 - (bitIndex + k)), 0)
            val shiftLeft = minOf(k, 8 - bitIndex)
            return (number shr shiftRight) and ((1 shl shiftLeft) - 1)
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
            So to be able to store the number in binary in the first LSB we save values in [1..7]

         */
        val lsbPixel = result[0, 0]

        var lsb = numberOfBits - 1
        for (bit in 0 until numberOfChannels) {
            lsbPixel[bit] = lsbPixel[bit] and 0xFE.toByte() or (lsb and 1).toByte()
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
                    if (offset==45)
                    {
                        println()
                    }
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

        //the 3 + part is for storing the number of LSB's used in the first pixel (3 channels = 3 bytes) encoding the message
        val numberOfUsableBytesInImage = 3 + numberOfBytesInImage * this.numberOfBits / 8

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
        var lsb = getLSB(coverImage[0, 0])


       // var res = readKByte(lsb, 0, 4, coverImage)


       /* val size =ByteBuffer.wrap(res).int
        println(size)*/

        //res.forEach { print(it.toInt()) }

        val message  = readKByte(lsb,0,4+2,coverImage)

        message.forEach {
            println(it)
        }



        return ByteArray(5)
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
                    if (!hasReachedOffset) {
                        if (seekPointer == offset) {
                            hasReachedOffset = true
                            continue
                        } else {
                            seekPointer += lsb
                        }
                    } else {
                        val data = image[i, j][channel]

                        for (bit in lsb - 1 downTo 0) {
                            if (hasFinished) break


                            byte=Byte_set(byte, counter++ % 8, data[bit])
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

