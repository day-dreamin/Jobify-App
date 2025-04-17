@file:OptIn(ExperimentalFoundationApi::class)

package com.example.pairpa

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

//This funcion is called when the user wants to delete a review they made, checking if the user is the same one who made the review
fun deleteReview(name: String, content: String) {
    if (name != getDispName(auth.currentUser?.email.toString())) {
        Toast.makeText(ctx, "Cannot delete other's reviews!", Toast.LENGTH_SHORT).show()
        return
    }

    val query = database.reference.child("Reviews").orderByChild("dispName").equalTo(name)
    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (reviewSnapshot in dataSnapshot.children) {
                val review = reviewSnapshot.getValue(ReviewData::class.java)
                if (review != null && review.content == content) {
                    reviewSnapshot.ref.removeValue()
                    Toast.makeText(ctx, "Deleting review...", Toast.LENGTH_SHORT).show()
                    break
                }
            }
            // Add a delay of 1 second before calling populateReviews()
            Handler(Looper.getMainLooper()).postDelayed({
                populateReviews()
                Toast.makeText(ctx, "Review deleted", Toast.LENGTH_SHORT).show()
            }, 1500)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Firebase", "Error while iterating through reviews", databaseError.toException())
        }
    })
    populateReviews()
}

//This composable function displays a single review from the input parameters
@Composable
fun Review(name: String, text: String, stars: Float) {
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
                Image(
                    painter = painterResource(id = R.drawable.usericon),
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
                            text = name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        RatingBar(
                            value = stars,
                            style = RatingBarStyle.Stroke(),
                            onValueChange = {},
                            onRatingChanged = {},
                            spaceBetween = 1.dp,
                            size = 20.dp,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            // Show a confirmation dialog before deleting
                            deleteReview(name, text)
                        }
                )

            }

        }
    }
}

//This composable function allows us to preview a Review by a user
@Preview
@Composable
fun PreviewReview() {
    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        // Replace with your image id
                        painterResource(id = backGround.value)
                        ,contentScale = ContentScale.FillBounds
                    )

            )
            {
                Review(name = "lorem", text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas maximus sapien vitae dignissim hendrerit. Duis in mattis elit. Curabitur viverra tellus sit amet ex condimentum, a congue ipsum sodales. Nunc maximus quam id massa posuere, id egestas arcu rutrum. Mauris porta, quam non condimentum lobortis, ex dui mollis dui, varius tincidunt turpis enim non sem.", 4.5f)
            }
        }
    }
}