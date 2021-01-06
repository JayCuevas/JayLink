package com.jaydlc.jaylink.lib

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class SuperSocket(private val socket: Socket, private val logger: ILogger?) {
    private val inputStream = DataInputStream(socket.getInputStream())
    private val outputStream = DataOutputStream(socket.getOutputStream())
    private val TAG = "Socket_${socket.inetAddress.hostAddress}"

    fun readLine(): String {
        val data = inputStream.readUTF()
        logger?.debug(TAG, "Received data from client: $data")
        return data
    }

    fun send(data: String) {
        logger?.debug(TAG, "Sending data to client: $data")
        outputStream.writeUTF(data)
        outputStream.flush()
    }

    fun disconnect() = socket.close()
}