package com.example.bluetoothlowenergyapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scanDevices : StateFlow<List<BluetoothDevice>>
    val pairedDevices : StateFlow<List<BluetoothDevice>>

    // free ressources from memory
    fun startDiscovery()
    fun stopDiscovery()

    //create server when click on device this device has a server and accept connection to him => active connection and can exchange bytes
    fun startBluetoothServer() : Flow<ConnectionResult>
    fun release()
}