package com.example.pairpa.Chat

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import com.example.pairpa.PlaceHolder
import com.example.pairpa.User
import com.example.pairpa.auth
import com.example.pairpa.ctx
import com.example.pairpa.database
import com.example.pairpa.loggedIn
import com.example.pairpa.ui.theme.Blue80
import com.example.pairpa.ui.theme.Cyan
import com.example.pairpa.ui.theme.SkyBlue
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.util.Date
import java.util.regex.Pattern

/*@Composable
fun ChatScreen() {
    var (email, setEmail) = remember { mutableStateOf("") }
    var isChatOpen = remember { mutableStateOf(false) }
    val db = Firebase.firestore

    Column {
        if (!isChatOpen.value) {
            Column {
                TextField(
                    value = email,
                    onValueChange = { setEmail(it) },
                    label = { Text("Enter email") }
                )
                Button(onClick = {
                    isChatOpen.value = true
                    // Add chat to the page
                    db.collection("chats").add(mapOf(
                        "participants" to arrayListOf(email, Firebase.auth.currentUser?.email)
                    ))
                }) {
                    Text("Open Chat")
                }
            }
        } else {
            Chat(email)
        }
    }
}

@Composable
fun Chat(otherUserEmail: String) {
    // Set up Firebase Firestore to listen for messages
    val db = Firebase.firestore
    val messages = remember { mutableStateListOf<String>() }
    val chatId = remember { mutableStateOf("") }
    val snapshot = db.collection("chats")
        .whereEqualTo("participants", arrayListOf(otherUserEmail, auth.currentUser?.email))
        .addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.w("Chat", "Listen failed.", e)
                return@addSnapshotListener
            }
            querySnapshot?.documents?.forEach { document ->
                chatId.value = document.id
                db.collection("chats").document(document.id).collection("messages")
                    .orderBy("timestamp")
                    .addSnapshotListener { querySnapshot, e ->
                        if (e != null) {
                            Log.w("Chat", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        querySnapshot?.documents?.map { message ->
                            messages.add(message.getString("content")!!)
                        }
                    }
            }
        }

    Column {
        LazyColumn {
            items(messages) { message ->
                Text(message)
            }
        }
        var (messageText, setMessage) = remember { mutableStateOf("") }
        TextField(
            value = messageText,
            onValueChange = { setMessage(it) },
            label = { Text("Enter message") }
        )
        Button(onClick = {
            // Send message to Firestore
            db.collection("chats").document(chatId.value).collection("messages").add(mapOf(
                "sender" to auth.currentUser?.email,
                "content" to messageText,
                "timestamp" to FieldValue.serverTimestamp()
            ))
            messageText = ""
        }) {
            Text("Send")
        }
    }
}*/


/*@Composable
fun ChatScreen() {
    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser?.email ?: ""
    val chats = remember { mutableStateListOf<Chat>() }
    val isDialogOpen = remember { mutableStateOf(false) }
    val selectedChat = remember { mutableStateOf<Chat?>(null) }

    // Fetch existing chats from Firestore
    LaunchedEffect(Unit) {
        db.collection("chats")
            .whereArrayContains("participants", currentUser)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w("ChatScreen", "Listen failed.", e)
                    return@addSnapshotListener
                }

                chats.clear()
                querySnapshot?.documents?.forEach { document ->
                    val participants = document.get("participants") as List<String>
                    val otherUser = participants.firstOrNull { it != currentUser }
                    if (otherUser != null) {
                        chats.add(Chat(document.id, otherUser))
                    }
                }
            }
    }

    Column {
        // Display list of chats as cards
        LazyColumn {
            items(chats) { chat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedChat.value = chat
                            isDialogOpen.value = true
                        }
                ) {
                    Text(chat.otherUser, modifier = Modifier.padding(16.dp))
                }
            }
        }

        // Add new chat button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    selectedChat.value = null
                    isDialogOpen.value = true
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Add New Chat")
            }
        }
    }

    // Chat dialog
    if (isDialogOpen.value) {
        ChatDialog(
            selectedChat = selectedChat.value,
            onDismissRequest = { isDialogOpen.value = false },
            onSendMessage = { message, chatId ->
                db.collection("chats").document(chatId).collection("messages").add(
                    mapOf(
                        "sender" to currentUser,
                        "content" to message,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )
            },
            onCreateNewChat = { otherUserEmail ->
                val participants = arrayListOf(currentUser, otherUserEmail)
                db.collection("chats").add(mapOf("participants" to participants))
            }
        )
    }
}*/

@Composable
fun ChatScreen() {
    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser?.email ?: ""
    val chats = remember { mutableStateListOf<Chat>() }
    val isDialogOpen = remember { mutableStateOf(false) }
    val selectedChat = remember { mutableStateOf<Chat?>(null) }

    // Fetch existing chats from Firestore
    LaunchedEffect(Unit) {
        db.collection("chats")
            .whereArrayContains("participants", currentUser)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w("ChatScreen", "Listen failed.", e)
                    return@addSnapshotListener
                }

                chats.clear()
                querySnapshot?.documents?.forEach { document ->
                    val participants = document.get("participants") as List<String>
                    val otherUser = participants.firstOrNull { it != currentUser }
                    if (otherUser != null) {
                        // Fetch the most recent message for each chat
                        document.reference.collection("messages")
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener { messageSnapshot ->
                                val lastMessage = messageSnapshot.documents.firstOrNull()
                                val lastMessageTimestamp = lastMessage?.getTimestamp("timestamp")
                                chats.add(Chat(document.id, otherUser, lastMessageTimestamp))
                            }
                    }
                }
            }
    }

    // Sort the chats list based on the most recent message timestamp
    val sortedChats = chats.sortedByDescending { it.lastMessageTimestamp }

    if (!loggedIn) {
        PlaceHolder()
    }
    else {
        Column {
            // Display list of chats as cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        selectedChat.value = null
                        isDialogOpen.value = true
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Add New Chat")
                }
            }

            LazyColumn {
                items(sortedChats) { chat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedChat.value = chat
                                isDialogOpen.value = true
                            }
                    ) {
                        Text(chat.otherUser, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            // Add new chat butto
        }

        // Chat dialog
        if (isDialogOpen.value) {
            ChatDialog(
                selectedChat = selectedChat.value,
                onDismissRequest = { isDialogOpen.value = false },
                onSendMessage = { message, chatId ->
                    db.collection("chats").document(chatId).collection("messages").add(
                        mapOf(
                            "sender" to currentUser,
                            "content" to message,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                    )
                },
                onCreateNewChat = { otherUserEmail ->
                    val participants = arrayListOf(currentUser, otherUserEmail)
                    db.collection("chats").add(mapOf("participants" to participants))
                }
            )
        }
    }


}

@Composable
fun ChatDialog(
    selectedChat: Chat?,
    onDismissRequest: () -> Unit,
    onSendMessage: (String, String) -> Unit,
    onCreateNewChat: (String) -> Unit
) {
    val db = Firebase.firestore
    val messages = remember { mutableStateListOf<Message>() }
    val chatId = remember { mutableStateOf("") }
    val messageText = remember { mutableStateOf("") }
    val otherUserEmail = remember { mutableStateOf("") }

    if (selectedChat != null) {
        // Set up Firestore listener for messages in the selected chat
        LaunchedEffect(selectedChat.id) {
            chatId.value = selectedChat.id
            db.collection("chats").document(selectedChat.id).collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.w("ChatDialog", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    messages.clear()
                    querySnapshot?.documents?.forEach { document ->
                        val sender = document.getString("sender") ?: ""
                        val content = document.getString("content") ?: ""
                        messages.add(Message(sender, content))
                    }
                }
        }
    }

    /*AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                if (selectedChat != null) selectedChat.otherUser else "Add New Chat"
            )
        },
        text = {
            Column {
                if (selectedChat == null) {
                    TextField(
                        value = otherUserEmail.value,
                        onValueChange = { otherUserEmail.value = it },
                        label = { Text("Enter other user's email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                LazyColumn {
                    items(messages) { message ->
                        Text("${message.sender}: ${message.content}")
                    }
                }
                TextField(
                    value = messageText.value,
                    onValueChange = { messageText.value = it },
                    label = { Text("Enter message") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedChat == null && otherUserEmail.value.isNotBlank()) {
                        onCreateNewChat(otherUserEmail.value)
                    } else if (messageText.value.isNotBlank()) {
                        onSendMessage(messageText.value, chatId.value)
                        messageText.value = ""
                    }
                }
            ) {
                Text(if (selectedChat == null) "Create Chat" else "Send")
            }
        }
    )*/

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp, max = 100.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = if (selectedChat!= null) selectedChat.otherUser else "Add New Chat",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
                    modifier = Modifier.weight(1f)
                )
                if (selectedChat!= null) {
                    Button(
                        onClick = { selectedChat?.let { findUserByEmail(it.otherUser) } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Call")
                    }
                }
            }
            /*Text(
                if (selectedChat != null) selectedChat.otherUser else "Add New Chat"
            )*/
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (selectedChat == null) {
                        TextField(
                            value = otherUserEmail.value,
                            onValueChange = { otherUserEmail.value = it },
                            label = { Text("Enter other user's email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column {
                        messages.forEach { message ->
                            Text("${message.sender}: ${message.content}")
                        }
                    }
                    if (selectedChat != null) {
                        TextField(
                            value = messageText.value,
                            onValueChange = { messageText.value = it },
                            label = { Text("Enter message") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Dismiss")
                }
                Button(
                    onClick = {
                        if (selectedChat == null && otherUserEmail.value.isNotBlank()) {
                            if (!isValidEmail(otherUserEmail.value)) {
                                Toast.makeText(ctx, "Invalid email!", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                onCreateNewChat(otherUserEmail.value)
                            }
                            //onCreateNewChat(otherUserEmail.value)
                        } else if (messageText.value.isNotBlank()) {
                            onSendMessage(messageText.value, chatId.value)
                            messageText.value = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (selectedChat == null) "Create Chat" else "Send"
                    )
                }
            }
        }
    )
}

data class Chat(val id: String, val otherUser: String, val lastMessageTimestamp: Timestamp?)
data class Message(val sender: String, val content: String)

/*fun findUserByEmail(email: String) {
    val usersRef = database.getReference("Users")

    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var userFound = false

            for (userSnapshot in snapshot.children) {
                val user = userSnapshot.getValue(User::class.java)
                if (user?.email == email) {
                    userFound = true
                    val userNumber = user.number

                    if (userNumber.isNullOrEmpty() || !isNumeric(userNumber)) {
                        Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
                    } else {
                        // User number is available, do something with it
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userNumber))
                        dialIntent.flags = FLAG_ACTIVITY_NEW_TASK
                        ctx.startActivity(dialIntent)
                    }

                    break
                }
            }

            if (!userFound) {
                Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}*/

/*fun findUserByEmail(email: String) {
    val usersRef = database.getReference("Users")

    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var userFound = false

            for (userEntry in snapshot.children) {
                val user = userEntry.getValue(User::class.java)
                if (user?.email == email) {
                    userFound = true
                    val userNumber = user.number

                    if (userNumber.isNullOrEmpty() ||!isNumeric(userNumber)) {
                        Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
                    }  else {
                        // User number is available, do something with it
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userNumber))
                        dialIntent.flags = FLAG_ACTIVITY_NEW_TASK
                        ctx.startActivity(dialIntent)
                    }

                    break
                }
            }

            if (!userFound) {
                Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}*/

fun findUserByEmail(email: String) {
    val usersRef = database.getReference("Users")

    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var userFound = false

            for (userEntry in snapshot.children) {
                try {
                    val user = userEntry.getValue(User::class.java)
                    if (user?.email == email) {
                        userFound = true
                        val userNumber = user.number

                        if (userNumber.isNullOrEmpty() ||!isNumeric(userNumber)) {
                            Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
                        } else {
                            // User number is available, do something with it
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userNumber))
                            dialIntent.flags = FLAG_ACTIVITY_NEW_TASK
                            ctx.startActivity(dialIntent)
                        }

                        break
                    }
                } catch (e: DatabaseException) {
                    // Handle the exception here, for example:
                    Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
                }
            }

            if (!userFound) {
                Toast.makeText(ctx, "User does not have an associated number!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}

fun isValidEmail(email: String): Boolean {
    val pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
    return pattern.matcher(email).matches()
}

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}