package com.example.bluetoothlowenergyapp.domain

import com.example.bluetoothlowenergyapp.domain.chat.BluetoothMessage

sealed interface ConnectionResult {
    object ConnectionEstablished : ConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage) : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}