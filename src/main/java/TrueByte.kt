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
        if (bitCount < 0) return shl(-bitCount)
        val res = and255(value.toInt()) shr bitCount
        return TrueByte(res.toByte())
    }

    infix fun shl(bitCount: Int): TrueByte {
        if (bitCount < 0) return shr(-bitCount)
        val res = value.toInt() shl bitCount
        return TrueByte(and255(res).toByte())
    }

    private fun and255(int: Int): Int {
        return int and 255
    }


    override operator fun equals(other:Any?):Boolean{
        return when (other){
            null -> false
            is TrueByte -> this.value.equals(other.value)
            is Byte -> this.value.equals(other)
            is Int -> this.value.equals(other.toByte())
            else -> false
        }
    }


    override fun toString(): String{
        return and255(value.toInt()).toString()
    }

    operator fun get(position: Int): Boolean {
        require(position in 0..7)
        return (this shr position) and TrueByte(1) == TrueByte(1)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}