package com.example.pairpa
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

//This composable function allows users to type and submit a review with rating
@Composable
fun ReviewBox(){
    var desc by remember { mutableStateOf("") }
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
                    Text("Submit a review!", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.padding(5.dp))
                    var rating: Float by remember { mutableStateOf(5.0f) }
                    RatingBar(
                        value = rating,
                        style = RatingBarStyle.Stroke(),
                        onValueChange = {
                            rating = it
                        },
                        onRatingChanged = {}
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Comment") }
                    )
                    Button(onClick = {
                        submitReview(rating, desc)
                        rating = 5.0f
                        desc = ""
                        //Toast.makeText(ctx, "Review submitted!", Toast.LENGTH_SHORT).show()
                    }, content = {
                        Text("Add review", color = MaterialTheme.colorScheme.inverseSurface)
                    }, modifier = Modifier.padding(top = 5.dp).fillMaxWidth()
                        .neu(
                            lightShadowColor = DefaultShadowColor.copy(0.1f),
                            darkShadowColor = DefaultShadowColor,
                            shadowElevation = 5.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(12.dp)),
                        )
                        .height(40.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    )
                }
            }
        }
    }
}

//This composable function contains the layout of the page for the reviews tab
@Composable
fun ReviewLayout() {
    populateReviews()
    println(reviews[0][0])
    var num by remember {mutableStateOf(100)}
    Column (modifier = Modifier.fillMaxSize()){
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
        ) {
            item{
                ReviewBox()
                if (num == 100)
                    num++
                else
                    num--
            }
            if (num > 0) {
                items(reviews.size) { id ->
                    Review(
                        name = reviews[id][0] as String,
                        text = reviews[id][1] as String,
                        reviews[id][2] as Float
                        //owner = reviews[id][3] as String
                    )
                }
            }
        }
    }
}

//This composable function allows us to preview the Reviews page
@Preview
@Composable
fun PreviewReviewList() {
    MaterialTheme {
        Surface {
            bg {
                ReviewLayout()
            }
        }
    }
}