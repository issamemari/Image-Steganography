import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int


class Steganography : CliktCommand() {
    override fun run() = Unit
}

class Embed : CliktCommand(help = "Embed a file or a string in an image") {

    val numberOfBits: Int by option(help = "Number of least-significatn bits to use for encoding the message").int().default(1)
    val message: String by argument(help = "Message to encode")
    val inputImagePath: String by argument(help = "Path to the input image")
    val outputImagePath: String by argument(help = "Path to the output image")

    override fun run() {
        val steganographer = LSB(numberOfBits, 3)
        val coverImage: Image = steganographer.embed(message.toByteArray(), Image(inputImagePath))
        coverImage.export(outputImagePath)
        TermUi.echo("Cover image saved to " + outputImagePath)
    }
}

class Extract : CliktCommand(help = "Extract a file or a string from an image") {

    val inputImagePath: String by argument(help = "Path to the input image")

    override fun run() {
        val steganographer = LSB()
        val message: String = String(steganographer.extract(Image(inputImagePath)))
        TermUi.echo("Message is:\n" + message)
    }
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) = Steganography()
            .subcommands(Embed(), Extract())
            .main(args)
}