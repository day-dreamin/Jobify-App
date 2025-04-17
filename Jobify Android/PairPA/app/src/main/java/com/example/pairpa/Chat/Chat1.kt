package com.example.pairpa.Chat

/*import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddChatScreen(
    onAddChat: (String) -> Unit
) {
    //var email = remember { mutableStateOf("") }
    var (email) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(
                text = "Email",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (email.isNotEmpty()) {
                    onAddChat(email)
                    email = ""
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Chat")
        }
    }
}

@Composable
fun ChatListScreen(
    chats: List<Chat>,
    onOpenChat: (Chat) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(chats) { chat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChat(chat) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.otherUserEmail,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
                IconButton(
                    onClick = { /* Implement the "Open chat" action */ },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Message,
                        contentDescription = "Open chat"
                    )
                }
            }
        }
    }
}*/