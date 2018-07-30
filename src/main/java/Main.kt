import kotlin.experimental.and
import kotlin.experimental.or

fun main(args: Array<String>) {


/*    val k = 5
    var offset = 44

    val message: ByteArray = byteArrayOf(0,0, 0, 2, 49,50)
    println(getKBits(message, k, offset))*/



/*
    val b:Byte = 7
    // 7 in binary is 00000111
    val r = Byte_set(b,7,false);
    println(r)

*/
    //println(12.shl(8))

    val steganographer = LSB(5, 3)
    val coverImage: Image = steganographer.embed(byteArrayOf(49,50, 51, 52, 53, 54), Image("/home/issa/Desktop/red.bmp"))
    val retrievedMessage = steganographer.extract(coverImage)
    retrievedMessage.forEach {
        println(it)
    }
}

fun Byte_set(byte:Byte,position: Int,value:Boolean):Byte{
    if (value)
        return (byte or ((1 shl (7-position)).toByte()))
    else
        return byte and (1 shl (7-position)).inv().toByte()

}

