package com.example.pairpa.Chat

/*import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ChatScreen(
    onEmailEntered: (String) -> Unit,
    email: String,
    messages: List<String>,
    onMessageSent: (String) -> Unit
) {
    val firebase = Firebase.firestore
    val currentUser = FirebaseAuth.getInstance().currentUser

    Column {
        TextField(
            value = email,
            onValueChange = { onEmailEntered(it) },
            label = { Text("Enter email") }
        )
        Button(onClick = {
            // Add chat functionality here
            val chat = hashMapOf("email" to email, "message" to "Hello!")
            firebase.collection("chats").add(chat)
        }) {
            Text("Start Chat")
        }

        // Display the chat messages here
        ChatMessages(messages = messages)
    }

    // Add a listener to update the messages when a new message is added
    firebase.collection("chats").document(/* Add code to get the chat document ID here */).collection("messages")
        .addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ChatScreen", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messagesList = snapshot.documents.map { it.data }
                onMessageSent(messagesList.toString())
            } else {
                Log.d("ChatScreen", "Current data: null")
            }
        }
}

@Composable
fun ChatMessages(messages: List<String>) {
    LazyColumn {
        items(messages) { message ->
            Text(message)
        }
    }
}

@Composable
fun ChatScreenWrapper() {
    var email = remember { mutableStateOf("") }
    var messages = remember { mutableStateOf(listOf<String>()) }

    ChatScreen(
        onEmailEntered = { email = it },
        email = email,
        messages = messages,
        onMessageSent = { message ->
            messages = messages.plus(message)
        }
    )
}*/