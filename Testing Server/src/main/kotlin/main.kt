import com.jaydlc.jaylink.lib.ILogger
import com.jaydlc.jaylink.lib.Server

fun main(args: Array<String>) {
    var server = Server(8080, object : ILogger {
        override fun debug(tag: String, message: String) {
            println("$tag: $message")
        }

        override fun error(tag: String, message: String, error: Throwable?) {
            System.err.println("$tag: m")
        }
    })

    server.messages.subscribe { message ->
//        println(message)
    }

    server.listen()
}