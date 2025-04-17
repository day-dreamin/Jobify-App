package com.example.pairpa

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner

//This composable funcion is a dialog that shows details of a clothing item when the card is clicked on in the catalogue
@Composable
fun DialogWithImage(
    id: MutableState<Int>,
    visible: MutableState<Boolean>
) {
    if(visible.value) {
        val painter = painterResource(id = db[id.value][5] as Int)
        val name = db[id.value][0] as String
        val category = db[id.value][1] as String
        val size = db[id.value][2] as String
        val price = db[id.value][3] as String
        val desc = db[id.value][4] as String

        Dialog(onDismissRequest = {
            visible.value = false
        }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Image(
                            painter = painter,
                            contentDescription = name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp)),
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Column(modifier = Modifier.padding(7.dp)) {
                            Text(text = name, style = MaterialTheme.typography.headlineSmall)
                            Text(
                                text = "Category",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                category,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Price",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "$$price",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Size",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                size,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                    }
                    Text(modifier = Modifier.padding(16.dp),
                        text = desc,
                        style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(onClick = {
                            visible.value = false

                        }, content = {
                            Text("Close", color = MaterialTheme.colorScheme.inverseSurface)
                        }, modifier = Modifier.padding(8.dp)
                            .neu(
                                lightShadowColor = DefaultShadowColor.copy(0.1f),
                                darkShadowColor = DefaultShadowColor,
                                shadowElevation = 5.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Pressed(RoundedCorner(12.dp)),
                            )
                            .height(40.dp), shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                        )

                        Button(onClick = {

                            if (auth.currentUser == null) {
                                Toast.makeText(ctx, "Not logged in!", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val usersReference = database.getReference("Users")
                                val userUid = auth.currentUser?.uid.toString()
                                val userReference = usersReference.child(userUid)
                                val cartReference = userReference.child("cart")

                                val cartItem = CartData(name, price.toInt())
                                cartReference.push().setValue(cartItem)

                                Toast.makeText(ctx, "Added to cart", Toast.LENGTH_SHORT).show()

                            }
                            visible.value = false
                        }, content = {
                            Text("Add to cart", color = MaterialTheme.colorScheme.inverseSurface)
                        }, modifier = Modifier.padding(8.dp)
                            .neu(
                                lightShadowColor = DefaultShadowColor.copy(0.1f),
                                darkShadowColor = DefaultShadowColor,
                                shadowElevation = 5.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Pressed(RoundedCorner(12.dp)),
                            )
                            .height(40.dp), shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                        )
                    }

                }
            }
        }
    }
}

//This composable function allows us to preview the PriceBox
@Preview
@Composable
fun PreviewPrice() {
    val visible: MutableState<Boolean> =  remember { mutableStateOf(false) }
    val id: MutableState<Int> =  remember { mutableStateOf(0) }
    visible.value = true
    MaterialTheme {
        Surface {
            DialogWithImage(
                id = id,
                visible = visible
            )
        }
    }
}