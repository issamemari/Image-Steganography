fun main(args: Array<String>) {

    val action = args[0]

    if (action == "embed") {
        val numberOfBits = args[1].toInt()
        val message = args[2].toByteArray()
        val imagePath = args[3]
        val outputPath = args[4]

        val steganographer = LSB(numberOfBits, 3)
        val coverImage: Image = steganographer.embed(message, Image(imagePath))

        coverImage.export(outputPath)

    } else if (action == "extract") {
        val imagePath = args[1]
        val steganographer = LSB()
        val message: String = String(steganographer.extract(Image(imagePath)))
        println(message)
    }
}