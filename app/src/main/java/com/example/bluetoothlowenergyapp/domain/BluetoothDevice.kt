package com.example.bluetoothlowenergyapp.domain

// to avoid conflict between real BluetoothDevice and my model
typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val name: String?,
    val address:String
)
