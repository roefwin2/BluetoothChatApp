package com.example.bluetoothlowenergyapp.helper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.example.bluetoothlowenergyapp.domain.BluetoothDeviceDomain

@RequiresApi(VERSION_CODES.R)
@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}