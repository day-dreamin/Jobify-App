package com.example.pairpa

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.twotone.AccountBox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

//This box allows the users to input sign up or sign in details and sign up or sign in
@Composable
fun LoginBox(){
    var name by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(5.dp)
            .neu(
                lightShadowColor = DefaultShadowColor.copy(0.1f),
                darkShadowColor = DefaultShadowColor,
                shadowElevation = 5.dp,
                lightSource = LightSource.LEFT_TOP,
                shape = Flat(RoundedCorner(8.dp)),
            )
            .width(LocalConfiguration.current.screenWidthDp.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(5.dp)) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.TwoTone.AccountBox, "", modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth())

                    Spacer(modifier = Modifier.padding(5.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = name,
                        singleLine = true,
                        onValueChange = { name = it },
                        label = { Text("Email") }
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = pw,
                        onValueChange = { pw = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Button(onClick = {
                        login(name, pw)
                    }, content = {
                        Text("Login", color = MaterialTheme.colorScheme.inverseSurface)
                    }, modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .neu(
                            lightShadowColor = DefaultShadowColor.copy(0.1f),
                            darkShadowColor = DefaultShadowColor,
                            shadowElevation = 5.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Pressed(RoundedCorner(12.dp)),
                        )
                        .height(40.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    )
                    Button(onClick = {
                        signup(name, pw)
                    }, content = {
                        Text("Register", color = MaterialTheme.colorScheme.inverseSurface)
                    }, modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .neu(
                            lightShadowColor = DefaultShadowColor.copy(0.1f),
                            darkShadowColor = DefaultShadowColor,
                            shadowElevation = 5.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Pressed(RoundedCorner(12.dp)),
                        )
                        .height(40.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    )
                }
            }
        }
    }
}

//This composable function shows the profile of the user once they are signed in, with the user's account details
@Composable
fun ProfileBox() {
    var uri by remember { mutableStateOf<Uri?>(null) }

    val showEmail: String = auth.currentUser?.email.toString()

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )

    LaunchedEffect(uri) {
        if (uri != null) {
            try {
                Toast.makeText(ctx, "Updating profile...", Toast.LENGTH_SHORT).show()
                val downloadUrl = uploadImageToFirebase(uri!!)

                // Update the user's profile picture URL in Firebase Authentication
                val user = auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    photoUri = Uri.parse(downloadUrl)
                }
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(ctx, "Profile updated", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (_: Exception) {
            }
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(5.dp)
            .neu(
                lightShadowColor = DefaultShadowColor.copy(0.1f),
                darkShadowColor = DefaultShadowColor,
                shadowElevation = 5.dp,
                lightSource = LightSource.LEFT_TOP,
                shape = Flat(RoundedCorner(8.dp)),
            )
            .width(LocalConfiguration.current.screenWidthDp.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(5.dp)) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    val profileImageUrl = auth.currentUser?.photoUrl?.toString()
                    if (profileImageUrl != null) {
                        Log.d("lorem", profileImageUrl)
                    }
                    val imageUriForDisplay = uri ?: profileImageUrl.takeIf { it != null }?.let { ImageRequest.Builder(ctx).data(it).build() }


                    if (imageUriForDisplay == null) {

                        Icon(Icons.TwoTone.AccountBox, "", modifier = Modifier
                            .height(150.dp)
                            .width(150.dp)
                        )
                    }

                    else {

                        AsyncImage(
                            model = imageUriForDisplay,
                            contentDescription = null,
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                        )
                    }

                    //UserProfile(user = auth.currentUser)
                    //UserProfilePicture()

                    Spacer(modifier = Modifier.padding(5.dp))

                    Text(
                        text = "Email: $showEmail",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 19.sp
                    )



                    val usersReference = database.getReference("Users")
                    val userUid = auth.currentUser?.uid.toString()
                    val userReference = usersReference.child(userUid)
                    val addressReference = userReference.child("address")

                    val addressState = remember { mutableStateOf("Not set") }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 5.dp).padding(top = 5.dp)) {

                        PenWithDialog(addressState)

                        addressReference.get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                addressState.value = dataSnapshot.getValue(String::class.java)!!
                            } else {
                                // Handle the case where the address node doesn't exist
                            }
                        }.addOnFailureListener { _ ->
                            // Handle the case where the get operation fails
                        }

                        Text(
                            text = "Address: ${addressState.value}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp
                        )
                    }

                    //val usersReference = database.getReference("Users")
                    //val userUid = auth.currentUser?.uid.toString()
                    //val userReference = usersReference.child(userUid)
                    val numberReference = userReference.child("number")

                    val numberState = remember { mutableStateOf("Not set") }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 5.dp)) {

                        PenWithDialog2(numberState)

                        numberReference.get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                numberState.value = dataSnapshot.getValue(String::class.java)!!
                            } else {
                                // Handle the case where the address node doesn't exist
                            }
                        }.addOnFailureListener { _ ->
                            // Handle the case where the get operation fails
                        }

                        Text(
                            text = "Number: ${numberState.value}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp
                        )
                    }


                    Button(onClick = {
                        singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }, content = {
                        Text("Change profile picture", color = MaterialTheme.colorScheme.inverseSurface)
                    }, modifier = Modifier
                        .padding(top = 25.dp)
                        .fillMaxWidth()
                        .neu(
                            lightShadowColor = DefaultShadowColor.copy(0.1f),
                            darkShadowColor = DefaultShadowColor,
                            shadowElevation = 5.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Pressed(RoundedCorner(12.dp)),
                        )
                        .height(40.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    )

                    PurchaseButton()

                    Button(onClick = {
                        loggedIn = false
                        auth.signOut()
                        Toast.makeText(ctx, "Logged out", Toast.LENGTH_SHORT).show()
                        navController.navigate("main")
                    }, content = {
                        Text("Logout", color = MaterialTheme.colorScheme.inverseSurface)
                    }, modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .neu(
                            lightShadowColor = DefaultShadowColor.copy(0.1f),
                            darkShadowColor = DefaultShadowColor,
                            shadowElevation = 5.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Pressed(RoundedCorner(12.dp)),
                        )
                        .height(40.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    )
                }
            }
        }
    }
}

//This composable function displays a card of a single item in the user's purchase history to show in the purchase history
@Composable
fun PurchaseCard(purchase: Purchase) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(5.dp).neu(
            lightShadowColor = DefaultShadowColor.copy(0.1f),
            darkShadowColor = DefaultShadowColor,
            shadowElevation = 5.dp,
            lightSource = LightSource.LEFT_TOP,
            shape = Flat(RoundedCorner(8.dp)),
        ).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = purchase.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Price: $${purchase.price}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: ${purchase.date}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

//This compoase function is a dialog box that opens up and displays the user's purchase history
@Composable
fun PurchaseDialog(onDismissRequest: () -> Unit, purchases: List<Purchase>, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Purchase History") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                purchases.forEach { purchase ->
                    PurchaseCard(purchase)
                }
            }
        },
        modifier = Modifier.fillMaxSize().padding(16.dp),
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("Done")
            }
        }

    )
}

//This composable function is a button that opens the Purchase history dialog box on click
@Composable
fun PurchaseButton() {

    var PurchaseDialogState by remember { mutableStateOf(false) }
    var isClicked by remember {mutableStateOf(false)}
    var purchases by remember { mutableStateOf(emptyList<Purchase>()) }

    LaunchedEffect(Unit) {
        val userUid = auth.currentUser?.uid.toString()
        val userReference = database.getReference("Users").child(userUid).child("purchases")

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = mutableListOf<Purchase>()
                for (childSnapshot in dataSnapshot.children) {
                    val purchase = childSnapshot.getValue(Purchase::class.java)
                    if (purchase != null) {
                        list.add(purchase)
                    }
                }
                purchases = list.sortedByDescending { it.date }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    /*Button(onClick = {
        isClicked = true
        PurchaseDialogState = true
    }, content = {
        Text("Show Purchases", color = MaterialTheme.colorScheme.inverseSurface)
    }, modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
        .neu(
            lightShadowColor = DefaultShadowColor.copy(0.1f),
            darkShadowColor = DefaultShadowColor,
            shadowElevation = 5.dp,
            lightSource = LightSource.LEFT_TOP,
            shape = Pressed(RoundedCorner(12.dp)),
        )
        .height(40.dp), shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    )*/

    if (PurchaseDialogState) {
        PurchaseDialog(
            onDismissRequest = { PurchaseDialogState = false },
            purchases,
            onConfirm = {
                PurchaseDialogState = false
            }
        )
    }
}


//This compose function is a IconButton for editing field in the user's profile
@Composable
fun PenIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = "Pen icon"
        )
    }
}

//This compose function is a dialog box that accepts text from the user to change their account details
@Composable
fun TextInputDialog(onDismissRequest: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Enter text") },
        text = {
            //var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Text") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(text) }) {
                Text("Confirm")
            }
        }
    )
}

//This composable function text input dialog when the pen icon is clicked, using the functions from the pen icon and the text
//input dialog
@Composable
fun PenWithDialog(addressState: MutableState<String>) {
    var textInputDialogState by remember { mutableStateOf(false) }

    val usersReference = database.getReference("Users")
    val userUid = auth.currentUser?.uid.toString()
    val userReference = usersReference.child(userUid)
    val addressReference = userReference.child("address")



    Column {
        PenIcon(onClick = { textInputDialogState = true })

        if (textInputDialogState) {
            TextInputDialog(
                onDismissRequest = { textInputDialogState = false },
                onConfirm = { text ->
                    textInputDialogState = false
                    addressReference.setValue(text)
                    addressState.value = text
                    println(text)
                }
            )
        }
    }
}

//This second composable function for opening a dialog when the pen icon is clicked is for entering text into a different detail
//field, with more restrictions
@Composable
fun PenWithDialog2(numberState: MutableState<String>) {
    var textInputDialogState by remember { mutableStateOf(false) }

    val usersReference = database.getReference("Users")
    val userUid = auth.currentUser?.uid.toString()
    val userReference = usersReference.child(userUid)
    val numberReference = userReference.child("number")

    Column {
        PenIcon(onClick = { textInputDialogState = true })

        if (textInputDialogState) {
            TextInputDialog(
                onDismissRequest = { textInputDialogState = false },
                onConfirm = { text ->
                    if (!isNumeric(text)) {
                        Toast.makeText(ctx, "Please enter a valid number!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        textInputDialogState = false
                        numberReference.setValue(text)
                        numberState.value = text
                        println(text)
                    }
                }
            )
        }
    }
}

//This checks if a string is numeric so the user does not input invalid numbers
fun isNumeric(toCheck: String): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return toCheck.matches(regex)
}

//This function uploads the user's uploaded profile picture to the Firebase storage
private val storage = FirebaseStorage.getInstance()

suspend fun uploadImageToFirebase(imageUri: Uri): String {
    val imageRef = storage.reference.child("profile_pictures/${auth.currentUser!!.uid}")
    val uploadTask = imageRef.putFile(imageUri)
    val taskSnapshot = uploadTask.await()
    return taskSnapshot.storage.downloadUrl.await().toString()
}

//This function contains a Login box, and makes it scrollable. It also changes to a Profile box based on whether the user is logged in
@Composable
fun ScrollableLoginBox() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        if (auth.currentUser == null) {
            LoginBox()
        }
        else {
            ProfileBox()
        }
    }
}

//This composable function allows us to preview the Login/Profile box
@Preview
@Composable
fun PreviewLogin() {
    MaterialTheme {
        Surface {
            bg {
                ScrollableLoginBox()
            }
        }
    }
}