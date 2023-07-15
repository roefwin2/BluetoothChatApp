package com.example.bluetoothlowenergyapp.data.chat

import android.bluetooth.BluetoothSocket
import com.example.bluetoothlowenergyapp.domain.ConnectionResult
import com.example.bluetoothlowenergyapp.domain.ConnectionResult.TransferSucceeded
import com.example.bluetoothlowenergyapp.domain.chat.TransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    // an ACTIVE BLUETOOTH CONNECTION WITH ANOTHER DEVICE
    private val socket: BluetoothSocket
) {
    fun listeningForIncomingMessage(): Flow<ConnectionResult> = flow {
        if (!socket.isConnected) {
            return@flow
        }
        val buffer = ByteArray(1024)
        //keep this connection alive to listen from incoming message because we can always get a message
        while (true) {
            val byteCount = try {
                // read incoming data and put it in buffer
                // don't execute this on main thread to be sure flowOn Possibly blocking call in non-blocking context could lead to thread starvation
                socket.inputStream.read(buffer)
            } catch (e: IOException) {
                throw TransferFailedException()
            }
            emit(
                TransferSucceeded(
                    message = buffer.decodeToString(endIndex = byteCount)
                        .toBluetoothMessage(isFromCurrentUser = false)
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    suspend fun sendMessage(byteArray: ByteArray): Boolean {
        // use withContext to switch thread and not use main thread
        return withContext(Dispatchers.IO) {
            try {
                // send somthing out ot the other party
                socket.outputStream.write(byteArray)
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext false
            }
            // after this block if we not send a exception return true
            true
        }
    }
}