package com.example.pairpa

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner

//The card that shows the about section in the app
@Composable
fun showabout(){
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
            .width((LocalConfiguration.current.screenWidthDp).dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Blossom Boutique", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.padding(5.dp))
            Text("Welcome to Blossom Boutique, where fashion meets convenience! As an emerging and trendsetting fashion retailer, we seek to revolutionise your shopping experience. Embrace the future of fashion with our custom mobile app, allowing you to effortlessly browse our exquisite clothing line from the comfort of your own space.\n\nHere, we aren't just a store; we're a fashion destination seeking to offer unparalleled convenience without compromising on style.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.padding(10.dp))
            Text("Contact details", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.padding(5.dp))
            Text("Phone number", style = MaterialTheme.typography.bodyLarge)
            Text("+65 91152979", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.padding(5.dp))
            Text("Email", style = MaterialTheme.typography.bodyLarge)
            Text("blossom.boutique.2026@gmail.com", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.padding(5.dp))
            Text("Address", style = MaterialTheme.typography.bodyLarge)
            Text("20 Clementi Ave 1, Singapore 129957", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.padding(5.dp))

            Button(onClick = {
                val location: Uri = Uri.parse("geo:0,0?q=" + Uri.encode("NUS High School of Math and Science")) // z param is zoom level
                val intent = Intent(Intent.ACTION_VIEW, location)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ctx.startActivity(intent)

            }, content = {
                Text("Visit us!", color = MaterialTheme.colorScheme.inverseSurface)
            }, modifier = Modifier.padding(top = 5.dp)
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

//This function makes the card scrollable so the users can scroll to read the whole card
@Composable
fun ScrollableAbout() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        showabout()
    }
}

//A preview of the About page
@Preview
@Composable
fun PreviewAbt(){
    popupvisible =  remember { mutableStateOf(false) }
    clickedid =  remember { mutableStateOf(0) }
    navController = rememberNavController()
    generate{
        CollapsibleTb("le shoppe", {
            bg {
                showabout()
            }
        })
    }
}