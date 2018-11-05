import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import org.apache.commons.io.FileUtils
import java.io.FileInputStream
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Paths
import java.util.*


class Steganography : CliktCommand() {
    override fun run() = Unit
}

class Embed : CliktCommand(help = "Embed a file or a string in an image") {

    val numberOfBits: Int by option(help = "Number of least-significatn bits to use for encoding the message. Must be between 1 and 8.").int().default(1)
    val messageFilePath: String by option(help = "Path to the file to hide in the image.").default("")
    val message: String by option(help = "Message to encode.").default("")
    val inputImagePath: String by argument(help = "Path to the input image.")
    val outputImagePath: String by argument(help = "Path to the output image. Output image format must be tiff, bmp, gif, wbmp, or png.")

    val AllowedOutputFormats = arrayOf<String>("tiff", "bmp", "gif", "wbmp", "png")

    fun String.getExtension() = this.substringAfterLast('.', "")

    override fun run() {

        if (!AllowedOutputFormats.contains(outputImagePath.getExtension())) {
            TermUi.echo("${outputImagePath.getExtension()} is an unsupported output format.\nSee help for embed command.")
            return
        }

        val steganographer = LSB(numberOfBits, 3)
        var coverImage: Image = Image(inputImagePath).copy()
        if (message != "") {
            coverImage = steganographer.embed(message.toByteArray(), Image(inputImagePath))
        } else if (messageFilePath != "") {
            val inputStream = FileInputStream(messageFilePath)
            val fileBytes: ByteArray = IOUtils.toByteArray(inputStream)
            val fileName = Paths.get(messageFilePath).fileName.toString()

            val fileNameLengthBytes = fileName.length
            val buffer = ByteBuffer.allocate(4)
            buffer.putInt(fileNameLengthBytes)
            val sizeByteArray = buffer.array()

            val fileNameBytes = fileName.toByteArray()
            coverImage = steganographer.embed(sizeByteArray + fileNameBytes + fileBytes, Image(inputImagePath))
        }
        coverImage.export(outputImagePath)
        TermUi.echo("Cover image saved to " + outputImagePath + ".")
    }
}

class Extract : CliktCommand(help = "Extract a file or a string from an image.") {

    val outputDirectoryPath: String by option(help = "Path to the directory where the hidden file will be output.").default("")
    val inputImagePath: String by argument(help = "Path to the input image.")

    override fun run() {
        if (outputDirectoryPath == "") {
            val steganographer = LSB()
            val message: String = String(steganographer.extract(Image(inputImagePath)))
            TermUi.echo(message)
        } else {
            val steganographer = LSB()
            val message: ByteArray = steganographer.extract(Image(inputImagePath))
            val fileNameLengthBytes = Arrays.copyOfRange(message, 0, 4)
            val fileNameLength = ByteBuffer.wrap(fileNameLengthBytes).int
            val fileNameBytes = Arrays.copyOfRange(message, 4, 4 + fileNameLength)
            val fileName = String(fileNameBytes)
            val fileBytes = Arrays.copyOfRange(message, 4 + fileNameLength, message.size)

            val currentPath = Paths.get(outputDirectoryPath)
            val filePath = Paths.get(currentPath.toString(), fileName).toString()
            FileUtils.writeByteArrayToFile(File(filePath), fileBytes)
            TermUi.echo("Output file saved to " + filePath + ".")
        }
    }
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) = Steganography()
            .subcommands(Embed(), Extract())
            .main(args)
}