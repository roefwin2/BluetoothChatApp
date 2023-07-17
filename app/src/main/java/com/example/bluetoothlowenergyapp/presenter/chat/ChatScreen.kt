package com.example.bluetoothlowenergyapp.presenter.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.bluetoothlowenergyapp.domain.chat.BluetoothMessage
import com.example.bluetoothlowenergyapp.presenter.BluetoothUIState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    state: BluetoothUIState,
    onDisconnect: (() -> Unit),
    onSendMessage: ((String) -> Unit)
) {
    val message = rememberSaveable {
        mutableStateOf("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current // to hide the keyboard after send message

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Messages", modifier = Modifier.weight(1f))
            IconButton(onClick = { onDisconnect.invoke() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Disconnect")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(state.messages) { bluetoothMessage: BluetoothMessage ->
                //Column to have the choice on the left or the right
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)) {
                    ChatMessage(
                        message = bluetoothMessage,
                        modifier = Modifier.align(if (bluetoothMessage.isFromCurrentUser) Alignment.End else Alignment.Start)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(.16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = message.value, onValueChange = {
                message.value = it
            }, modifier = Modifier.weight(1f), placeholder = {
                Text(text = "Message")
            })
            IconButton(onClick = {
                onSendMessage.invoke(message.value)
                message.value = ""
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message")
            }
        }

    }
}