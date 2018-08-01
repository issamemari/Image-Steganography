class TrueByte(var value: Byte) {


    infix fun and()

    infix fun shr(bitCount: Int): TrueByte {
        if (bitCount < 0) return shr(-bitCount)
        val res = value.toInt() shr bitCount
        return TrueByte(and255(res).toByte())
    }

    infix fun shl(bitCount: Int): TrueByte {
        if (bitCount < 0) return shr(-bitCount)
        val res = value.toInt() shl bitCount
        return TrueByte(and255(res).toByte())
    }

    private fun and255(int: Int): Int {
        return int and 255
    }


}