package com.example.bluetoothlowenergyapp.domain.chat

data class BluetoothMessage(
    val message: String,
    val senderName: String,
    val isFromCurrentUser: Boolean
)
