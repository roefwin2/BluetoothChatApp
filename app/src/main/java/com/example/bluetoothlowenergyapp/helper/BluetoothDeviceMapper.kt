package com.example.bluetoothlowenergyapp.helper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import com.example.bluetoothlowenergyapp.domain.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain() : BluetoothDeviceDomain{
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}