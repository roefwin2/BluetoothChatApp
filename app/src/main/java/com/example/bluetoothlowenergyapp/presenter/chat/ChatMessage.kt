package com.example.bluetoothlowenergyapp.presenter.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluetoothlowenergyapp.domain.chat.BluetoothMessage
import com.example.bluetoothlowenergyapp.presenter.activities.ui.theme.BluetoothLowEnergyAppTheme

@Composable
fun ChatMessage(
    message: BluetoothMessage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromCurrentUser) 15.dp else 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 15.dp,
                    bottomEnd = if (message.isFromCurrentUser) 0.dp else 15.dp
                )
            )
            .background(if (message.isFromCurrentUser) Color.Cyan else Color.Magenta)
    ) {
        Text(text = message.senderName, fontSize = 10.sp, color = Color.Black)
        Text(text = message.message, color = Color.Black, modifier = Modifier.widthIn(250.dp)) // Not exceddd 250 dp
    }
}

@Preview
@Composable
fun ChatMessagePreview() {
    BluetoothLowEnergyAppTheme() {
        ChatMessage(message = BluetoothMessage("Hello la France", "Xiaomi", true))
    }
}