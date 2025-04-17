package com.example.pairpa

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner

lateinit var popupvisible: MutableState<Boolean>
lateinit var clickedid: MutableState<Int>

//This composable function just contains a card containing the Clothing item image
//and a short title. It can be clicked to create a new popup, allowing user to view the details
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardView(id: Int = 0) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(5.dp).neu(
            lightShadowColor = DefaultShadowColor.copy(0.1f),
            darkShadowColor = DefaultShadowColor,
            shadowElevation = 5.dp,
            lightSource = LightSource.LEFT_TOP,
            shape = Flat(RoundedCorner(8.dp)),
        ).width((LocalConfiguration.current.screenWidthDp/2).dp),
        onClick = {
            clickedid.value = id
            popupvisible.value = true
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                Image(
                    painter = painterResource(id = db[id][5] as Int),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth,

                )
                Spacer(modifier = Modifier.padding(5.dp))
                Column {
                    Text(
                        text = db[id][0] as String,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}

//This function allows us to preview the Card for the Clothing item in the catalogue
@Preview
@Composable
fun PreviewCardView() {
    popupvisible =  remember { mutableStateOf(false) }
    clickedid =  remember { mutableStateOf(0) }
    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize()
                        .paint(
                            // Replace with your image id
                            painterResource(id = backGround.value)
                            ,contentScale = ContentScale.FillBounds
                        )
                )
            {
                CardView(0)
                DialogWithImage(
                    id = clickedid,
                    visible = popupvisible
                )
            }
        }
    }
}