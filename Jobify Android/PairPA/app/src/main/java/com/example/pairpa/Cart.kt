package com.example.pairpa

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//This composable function contains the page for the "Shopping Cart", displaying
//items in the user's cart as well as a checkout button that lets the user know how much the total is
@Composable
fun CartLayout() {
    var total by remember { mutableStateOf(0) }
    val cartItems = remember { mutableStateListOf<CartData>() }

    if (!loggedIn) {
        PlaceHolder()
    } else {
        LaunchedEffect(Unit) {
            val usersReference = database.getReference("Users")
            val userUid = auth.currentUser?.uid.toString()
            val userReference = usersReference.child(userUid)
            val cartReference = userReference.child("cart")

            val dataSnapshot = cartReference.get().await()
            cartItems.clear()
            for (itemSnapshot in dataSnapshot.children) {
                val cartData = itemSnapshot.getValue(CartData::class.java)
                if (cartData != null) {
                    cartItems.add(cartData)
                    total += cartData.price
                }
            }
        }

        val scrollState = rememberScrollState()

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            , horizontalAlignment = Alignment.CenterHorizontally) {
            if (cartItems.isEmpty()) {
                PlaceHolder2()
            }
            else {
                for (product in cartItems) {
                    Cart(product.itemName, product.price)
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Button(onClick = {

                    val PurchaseList : ArrayList<Purchase> = ArrayList<Purchase>()
                    val currentDate = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    for (item in cartItems) {
                        val formattedDate = formatter.format(currentDate)
                        val item2 = Purchase(item.itemName, item.price, formattedDate.toString())
                        PurchaseList.add(item2)
                    }

                    cartItems.clear()
                    val userReference = database.getReference("Users").child(auth.currentUser?.uid.toString())
                    val cartReference = userReference.child("cart")
                    val purchaseRef = userReference.child("purchases")


                    cartReference.setValue(ArrayList<CartData>())

                    val ogPurchaseList: ArrayList<Purchase> = ArrayList<Purchase>()
                    purchaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // Create an ArrayList to hold the purchases

                            // Iterate through the child nodes (i.e., the purchases)
                            for (purchaseSnapshot in dataSnapshot.children) {
                                // Get the Purchase object from the child node
                                val purchase = purchaseSnapshot.getValue(Purchase::class.java)
                                if (purchase != null) {
                                    ogPurchaseList.add(purchase)
                                }
                            }
                            val arrayListRes : ArrayList<Purchase> = ArrayList<Purchase>()
                            arrayListRes.addAll(ogPurchaseList)
                            arrayListRes.addAll(PurchaseList)
                            purchaseRef.setValue(arrayListRes)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle errors here
                        }
                    })


                    Toast.makeText(ctx, "Checked out!", Toast.LENGTH_SHORT).show()
                }, content = {
                    Image(
                        //ToDo: make the image the name of the shirt
                        painter = painterResource(id = R.drawable.baseline_shopping_cart_checkout_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Text("Check out: $$total", color = MaterialTheme.colorScheme.inverseSurface, fontSize = 20.sp)
                }, modifier = Modifier
                    .width(343.dp)
                    .neu(
                        lightShadowColor = DefaultShadowColor.copy(0.1f),
                        darkShadowColor = DefaultShadowColor,
                        shadowElevation = 5.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                    .height(40.dp), shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }
}

//This composable function creates a cart item based on its input, allowing us to make multiple cart items and display
//them from the user's cart items
@Composable
fun Cart(itemName: String, price: Int) {
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
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Row {
                val itemID = ctx.resources.getIdentifier(nameToimageName(itemName), "drawable", ctx.packageName)
                Image(
                    painter = painterResource(id = itemID),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Column {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = itemName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Text(
                        text = "$"+price.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
            }

        }
    }
}

//This placeholder acts as a placeholder to show in the cart page when the user is not logged in
@Composable
fun PlaceHolder() {
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
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_warning_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(85.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Not logged in!",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 30.sp
                    )
                }
            }

        }
    }
}

@Composable
fun PlaceHolder3() {
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
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier.padding(100.dp))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_downloading_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(85.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Recomposing...",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 30.sp
                    )
                }
            }

        }
    }
}

//This placeholder acts as a placeholder to show in the cart page when the user's cart is empty
@Composable
fun PlaceHolder2() {
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
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 22.sp
                    )
                }
            }

        }
    }
}

//This function allows us to obtain images of clothes in the resources from the name of the item quickly
fun nameToimageName(itemName: String): String{
    return itemName.replace(" ", "").lowercase()
}