package com.example.bluetoothlowenergyapp.data

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

//Boradcastreceiver when android operatin systeme find a device after discovery => fire boradcast
class BluetoothStateReceiver(
    private val onStateChange: (isConnected: Boolean, BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val device = if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                // this availbel efor tiramisu
                BluetoothDevice::class.java
            )
        } else {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE
            )
        }
        when (intent?.action) {
            // action attached when android find the device => retrieve the device info
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                onStateChange.invoke(true, device ?: return)
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                onStateChange.invoke(false, device ?: return)
            }
        }
    }

}