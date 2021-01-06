package com.jaydlc.jaylink.lib

// Resources:
// https://www.tutorialspoint.com/sending-and-receiving-data-with-sockets-in-android

import com.beust.klaxon.Klaxon
import io.reactivex.rxjava3.core.Emitter
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.intellij.lang.annotations.Flow
import java.net.BindException
import java.net.ServerSocket

class Server(
    val port: Int,
    private val logger: ILogger?,
) {
    private val TAG = "SocketServer"
    private val klaxon = Klaxon()

    private lateinit var server: ServerSocket
    private lateinit var client: SuperSocket

    val messages = PublishSubject.create<String>()
    private var listening = false

    @Throws(BindException::class)
    fun listen() {
        logger?.debug(TAG, "Creating server on port $port")
        server = ServerSocket(port)
        server.reuseAddress = true

        logger?.debug(
            TAG,
            "Server started on localhost:" + server.localPort.toString()
        )

        client = SuperSocket(server.accept(), logger)


        listening = true


        var data = ""
        do {
            data = client.readLine()

            messages.onNext(data)
        } while (data != "DISCONNECT")

        client.disconnect()
    }
}






