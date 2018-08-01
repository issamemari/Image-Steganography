import org.junit.Test

class TrueByteTest {
    @Test
    fun shifting() {

        val b = TrueByte(0b11111111.toByte())

        assert(b shl -9 == TrueByte(0b00000000.toByte()))
        assert(b shl -8 == TrueByte(0b00000000.toByte()))
        assert(b shl -7 == TrueByte(0b00000001.toByte()))
        assert(b shl -6 == TrueByte(0b00000011.toByte()))
        assert(b shl -5 == TrueByte(0b00000111.toByte()))
        assert(b shl -4 == TrueByte(0b00001111.toByte()))
        assert(b shl -3 == TrueByte(0b00011111.toByte()))
        assert(b shl -2 == TrueByte(0b00111111.toByte()))
        assert(b shl -1 == TrueByte(0b01111111.toByte()))
        assert(b shl 0 == TrueByte(0b11111111.toByte()))
        assert(b shl 1 == TrueByte(0b11111110.toByte()))
        assert(b shl 2 == TrueByte(0b11111100.toByte()))
        assert(b shl 3 == TrueByte(0b11111000.toByte()))
        assert(b shl 4 == TrueByte(0b11110000.toByte()))
        assert(b shl 5 == TrueByte(0b11100000.toByte()))
        assert(b shl 6 == TrueByte(0b11000000.toByte()))
        assert(b shl 7 == TrueByte(0b10000000.toByte()))
        assert(b shl 8 == TrueByte(0b00000000.toByte()))
        assert(b shl 9 == TrueByte(0b00000000.toByte()))

        assert(b shr -9 == TrueByte(0b00000000.toByte()))
        assert(b shr -8 == TrueByte(0b00000000.toByte()))
        assert(b shr -7 == TrueByte(0b10000000.toByte()))
        assert(b shr -6 == TrueByte(0b11000000.toByte()))
        assert(b shr -5 == TrueByte(0b11100000.toByte()))
        assert(b shr -4 == TrueByte(0b11110000.toByte()))
        assert(b shr -3 == TrueByte(0b11111000.toByte()))
        assert(b shr -2 == TrueByte(0b11111100.toByte()))
        assert(b shr -1 == TrueByte(0b11111110.toByte()))
        assert(b shr 0 == TrueByte(0b11111111.toByte()))
        assert(b shr 1 == TrueByte(0b01111111.toByte()))
        assert(b shr 2 == TrueByte(0b00111111.toByte()))
        assert(b shr 3 == TrueByte(0b00011111.toByte()))
        assert(b shr 4 == TrueByte(0b00001111.toByte()))
        assert(b shr 5 == TrueByte(0b00000111.toByte()))
        assert(b shr 6 == TrueByte(0b00000011.toByte()))
        assert(b shr 7 == TrueByte(0b00000001.toByte()))
        assert(b shr 8 == TrueByte(0b00000000.toByte()))
        assert(b shr 9 == TrueByte(0b00000000.toByte()))
    }

    @Test
    fun anding() {
        var a = TrueByte(0b10101010.toByte())
        var b = TrueByte(0b01010101.toByte())

        assert(a and b == TrueByte(0b00000000.toByte()))

        a = TrueByte(0b11111111.toByte())
        b = TrueByte(0b00000000.toByte())

        assert(a and b == TrueByte(0b00000000.toByte()))

        a = TrueByte(0b11111111.toByte())
        b = TrueByte(0b11111111.toByte())

        assert(a and b == TrueByte(0b11111111.toByte()))
    }

    @Test
    fun oring() {

        var a = TrueByte(0b10101010.toByte())
        var b = TrueByte(0b01010101.toByte())
        assert(a or b == TrueByte(0b11111111.toByte()))

        a = TrueByte(0b11111111.toByte())
        b = TrueByte(0b00000000.toByte())
        assert(a or b == TrueByte(0b11111111.toByte()))

        a = TrueByte(0b00000000.toByte())
        b = TrueByte(0b00000000.toByte())
        assert(a or b == TrueByte(0b00000000.toByte()))
    }


    @Test
    fun xoring() {

        var a = TrueByte(0b10101010.toByte())
        var b = TrueByte(0b01010101.toByte())
        assert(a xor b == TrueByte(0b11111111.toByte()))

        a = TrueByte(0b11111111.toByte())
        b = TrueByte(0b00000000.toByte())
        assert(a xor b == TrueByte(0b11111111.toByte()))

        a = TrueByte(0b00000000.toByte())
        b = TrueByte(0b00000000.toByte())
        assert(a xor b == TrueByte(0b00000000.toByte()))

        a = TrueByte(0b11111111.toByte())
        b = TrueByte(0b11111111.toByte())
        assert(a xor b == TrueByte(0b00000000.toByte()))

        a = TrueByte(0b10101010.toByte())
        b = TrueByte(0b10101010.toByte())
        assert(a xor b == TrueByte(0b00000000.toByte()))
    }

    @Test
    fun inverting() {

        var b = TrueByte(0b01010101.toByte())
        assert(b.inv() == TrueByte(0b10101010.toByte()))

        b = TrueByte(0b11111111.toByte())
        assert(b.inv() == TrueByte(0b00000000.toByte()))

        b = TrueByte(0b00000000.toByte())
        assert(b.inv() == TrueByte(0b11111111.toByte()))
    }

    @Test
    fun toStringing() {

        var b = TrueByte(0b01010101.toByte())
        assert(b.toString() == "85")

        b = TrueByte(0b11010101.toByte())
        assert(b.toString() == "213")

        b = TrueByte(0b11111111.toByte())
        assert(b.toString() == "255")

        b = TrueByte(0b00000000.toByte())
        assert(b.toString() == "0")
    }
    
    @Test
    fun equalsing() {

        var b = TrueByte(0b01010101.toByte())
        assert(b.equals(85))
        assert(b.equals(85.toByte()))
        assert(b.equals(TrueByte(85.toByte())))
        assert(!b.equals(85.0))
        assert(!b.equals(85.toLong()))
    }
}