package com.example.bluetoothlowenergyapp.data.chat

import com.example.bluetoothlowenergyapp.domain.chat.BluetoothMessage

fun String.toBluetoothMessage(isFromCurrentUser: Boolean): BluetoothMessage {
    val name = substringBeforeLast("#")
    val message = substringAfterLast("#")
    return BluetoothMessage(message, name, isFromCurrentUser)
}

fun BluetoothMessage.toByteArray(): ByteArray {
    //# to distingish the 2 fiels
    return "$senderName#$message".encodeToByteArray()
}