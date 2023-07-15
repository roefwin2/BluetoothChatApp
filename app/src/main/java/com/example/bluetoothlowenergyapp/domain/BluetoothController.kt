package com.example.bluetoothlowenergyapp.domain

import com.example.bluetoothlowenergyapp.domain.chat.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val isConnected: StateFlow<Boolean>
    val scanDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val errors: SharedFlow<String>

    // free ressources from memory
    fun startDiscovery()
    fun stopDiscovery()

    //create server when click on device this device has a server and accept connection to him => active connection and can exchange bytes
    fun startBluetoothServer(): Flow<ConnectionResult>

    //executed by party B wich connects to a device that has launch a server
    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult>

    suspend fun trySendMessage(message:String): BluetoothMessage? // sucess to send messege return Bluetooth messege et can add it in viewmdoel to refresh UI

    fun closeConnection()
    fun release()
}