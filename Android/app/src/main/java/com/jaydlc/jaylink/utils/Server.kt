package com.jaydlc.jaylink.utils

// Resources:
// https://www.tutorialspoint.com/sending-and-receiving-data-with-sockets-in-android

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import java.io.DataInputStream
import java.lang.Exception
import java.net.BindException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Server : Service() {
    private val TAG = "SocketServer"
    private val backupPort = 8081
    private lateinit var server: ServerSocket
    private lateinit var client: Socket
    private var ipAddress: String? = null

    fun getLocalIpAddress(context: Context): String? {
        if (!ipAddress.isNullOrEmpty()) {
            return ipAddress
        }

        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipInt = wifiInfo.ipAddress
        ipAddress = InetAddress.getByAddress(
                ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()
        ).hostAddress

        return ipAddress
    }

    private fun listen(port: Int) {
        server = ServerSocket(port)
        server.reuseAddress = true

        Log.d(TAG, "Server started on ${ipAddress ?: "localhost"}:" + server.localPort.toString())
        client = server.accept()

        val inputStream = DataInputStream(client.getInputStream())
        val data = inputStream.readUTF()
        Log.d(TAG, data)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val defaultPort = 8080
        val port = intent?.getIntExtra("EXTRA_PORT", defaultPort) ?: defaultPort

        Thread {
            try {
                listen(port)
            } catch (e: BindException) {
                listen(backupPort)
            }
        }.start()

        // Restart the service if it is killed
        // return START_STICKY

        // Will restart the service with the previous intent
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        Log.d(TAG, "Server is being killed...")

        // Closing the server will throw an exception if it is blocked waiting for a client
        try {
            server.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing server socket", e)
        }
        super.onDestroy()
    }
}