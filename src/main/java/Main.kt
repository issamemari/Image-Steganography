import java.math.BigInteger

fun main(args: Array<String>) {

    val k = 2
    var offset = 1
    // Get first k bits
    println(100 shr (8 - (offset * k)))

    // Get second k bits
    offset = 2
    println((100 shr (8 - (offset * k))) and ((1 shl k) - 1))

    // Get third k bits
    offset = 3
    println((100 shr (8 - (offset * k))) and ((1 shl k) - 1))

    // Get fourth k bits
    offset = 4
    println((100 shr (8 - (offset * k))) and ((1 shl k) - 1))
}
