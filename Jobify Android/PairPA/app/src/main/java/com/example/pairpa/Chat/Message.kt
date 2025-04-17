package com.example.pairpa.Chat

/*import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pairpa.User
import com.google.firebase.Timestamp

@Composable
fun DirectMessageScreen(
    user: User,
    onSendMessage: (String) -> Unit
) {
    Column {
        Text(user.email)
        MessageList(
            messages = user.messages,
            onSendMessage = onSendMessage
        )
    }
}

@Composable
fun MessageList(
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message, onSendMessage)
        }
    }
}

@Composable
fun MessageCard(
    message: Message,
    onSendMessage: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSendMessage(message.content)
            }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

data class Message(
    val content: String,
    val timestamp: Timestamp = Timestamp.now()
)*/