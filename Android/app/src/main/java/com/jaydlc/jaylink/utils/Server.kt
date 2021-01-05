package com.jaydlc.jaylink.utils

// Resources:
// https://www.tutorialspoint.com/sending-and-receiving-data-with-sockets-in-android

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import java.lang.Exception
import java.net.BindException
import java.net.InetAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import com.corundumstudio.socketio.*

class Server : Service() {
    private val TAG = "SocketServer"

    //    private val BACKUP_PORT = 8081
    private val server: SocketIOServer
    private val namespace: SocketIONamespace

    init {

        val config = Configuration()
        config.port = 8080
        config.hostname = "localhost"
        server = SocketIOServer(config)

        namespace = server.addNamespace("/")
    }

    private fun setListeners() {
        server.addConnectListener {
            val addr = it.handshakeData.address
        }
        namespace.addConnectListener {
//            i
        }
    }

    fun getLocalIpAddress(context: Context): String? {
        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipInt = wifiInfo.ipAddress
        val address = InetAddress.getByAddress(
            ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()
        ).hostAddress
        return address
    }

    private fun listen(port: Int) {
//        server = ServerSocket(port)
//        server.reuseAddress = true
//        Log.d(TAG, "Server started on 127.0.0.1:" + server.localPort.toString())
//        client = server.accept()
//        Log.d(TAG, "Received client!")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val defaultPort = 8080
        val port = intent?.getIntExtra("EXTRA_PORT", defaultPort) ?: defaultPort

        Thread {
            try {
                listen(port)
            } catch (e: BindException) {
//                listen(BACKUP_PORT)
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
//            server.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing server socket", e)
        }
        super.onDestroy()
    }
}