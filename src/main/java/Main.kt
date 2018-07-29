import java.math.BigInteger

// Function takes as input a byte array, returns k bits in the array starting from a given offset in bits
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

fun main(args: Array<String>) {

    val k = 5
    var offset = 22

    val message: ByteArray = byteArrayOf(0b00001100, 0b00010101, 0b00100011, 0b00101100, 0b00111011)
    println(getKBits(message, k, offset))

    //println(12.shl(8))
}