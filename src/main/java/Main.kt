fun main(args: Array<String>) {
    val steganographer = LSB(3, 3)
    val coverImage: Image = steganographer.embed(byteArrayOf(49, 50, 51, 52, 53, 54), Image("/home/issa/Desktop/red.bmp"))
    val retrievedMessage = steganographer.extract(coverImage)
    retrievedMessage.forEach {
        println(it)
    }
}