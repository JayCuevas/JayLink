package com.jaydlc.jaylink

// Resources:
// https://medium.com/mindorks/multiple-runtime-permissions-in-android-without-any-third-party-libraries-53ccf7550d0

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jaydlc.jaylink.utils.MessagingManager


class MainActivity : AppCompatActivity() {
    private final val PERMISSIONS_REQUEST = 1
    private lateinit var messageManager: MessagingManager

    private val requiredPermissions = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageManager = MessagingManager(contentResolver)

        if (checkAndRequestPermissions()) {
            main()
        }
    }

    fun main() {
        val messages = messageManager.getSmsMessages()

    }

    fun checkAndRequestPermissions(): Boolean {
        fun permissionGranted(permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                    this,
                    permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        val permissionsNeeded = mutableListOf<String>()

        for (perm in requiredPermissions) {
            if (!permissionGranted(perm)) {
                permissionsNeeded.add(perm)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    requiredPermissions,
                    PERMISSIONS_REQUEST
            )
            return false
        }

        return true
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {

        if (requestCode == PERMISSIONS_REQUEST) {
            val permissionResults = hashMapOf<String, Int>()
            var deniedCount = 0

            // Gather permission grant results
            for (i in grantResults.indices) {
                // Keep track of only permissions that were denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i])
                    deniedCount++
                }
            }

            // Check if all permissions are granted
            if (deniedCount == 0) {
                // Proceed with the app
                main()
            } else {
                for (entry in permissionResults.entries) {
                    val permName = entry.key
                    val permResult = entry.value

                    // Permission is denied (first time, when 'never ask again' is not checked)
                    // Ask again explaining the usage of the permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        // TODO: showDialog() is deprecated. Figure out better way to inform the user (me) why permissions are needed. YT link: https://youtu.be/PqRp3-t9GPM
                    }

                }
            }

        }
        // val permissionGranted =
        //     grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
        //
        // if (requestCode == PERMISSIONS_REQUEST) {
        //
        //     if (permissionGranted) {
        //         // refreshSmsInbox();
        //     } else {
        //         // TODO: Handle permission denied
        //     }
        //
        // } else {
        //     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // }
    }
}
