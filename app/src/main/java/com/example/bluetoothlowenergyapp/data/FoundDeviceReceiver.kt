package com.example.bluetoothlowenergyapp.data

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

//Boradcastreceiver when android operatin systeme find a device after discovery => fire boradcast
class FoundDeviceReceiver(
    private val onDeviceResult : (BluetoothDevice) -> Unit
) :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            // action attached when android find the device => retrieve the device info
            BluetoothDevice.ACTION_FOUND ->
            {
                val device  = if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        // this availbel efor tiramisu
                        BluetoothDevice::class.java
                    )
                } else {
                   intent.getParcelableExtra(
                       BluetoothDevice.EXTRA_DEVICE)
                }
                device?.let(onDeviceResult)
            }
        }
    }

}