import kotlin.experimental.and
import kotlin.experimental.or

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
        val shiftRight = maxOf((8 - (bitIndex + k)), 0)
        val shiftLeft = minOf(k, 8 - bitIndex)
        return (number shr shiftRight) and ((1 shl shiftLeft) - 1)
    }
}

fun main(args: Array<String>) {


    val k = 5
    var offset = 45

    val message: ByteArray = byteArrayOf(0,0, 0, 2, 49,50)
    println(getKBits(message, k, offset))


/*
    val b:Byte = 7
    // 7 in binary is 00000111
    val r = Byte_set(b,7,false);
    println(r)

*/
    //println(12.shl(8))
}

fun Byte_set(byte:Byte,position: Int,value:Boolean):Byte{
    if (value)
        return (byte or ((1 shl (7-position)).toByte()))
    else
        return byte and (1 shl (7-position)).inv().toByte()

}

