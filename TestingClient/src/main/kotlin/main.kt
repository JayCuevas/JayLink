import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

fun main(args: Array<String>) {
//    val host = "192.168.1.211"
    val host = "localhost"
    val socket = Socket(host, 8080)
    val input = DataInputStream(socket.getInputStream())
    val output = DataOutputStream(socket.getOutputStream())
    println("Sending data")
    output.writeUTF("Fuck")
    output.flush()

    output.writeUTF("Fuck")
    output.flush()
    output.writeUTF("Fuck")
    output.flush()
    output.writeUTF("Fuck")
    output.flush()

    output.writeUTF("DISCONNECT")
    output.flush()


    println("Disconnected")
}
