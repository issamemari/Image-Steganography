class TrueByte(val value: Byte) {


    infix fun and(trueByte: TrueByte):TrueByte{
        return TrueByte(and255(this.value.toInt() and trueByte.value.toInt()).toByte())
    }

    infix fun or(trueByte: TrueByte):TrueByte{
        return TrueByte(and255(this.value.toInt() or trueByte.value.toInt()).toByte())
    }

    infix fun xor(trueByte: TrueByte):TrueByte{
        return TrueByte(and255(this.value.toInt() xor trueByte.value.toInt()).toByte())
    }

    fun inv():TrueByte{
        return TrueByte(and255(this.value.toInt().inv()).toByte())
    }


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