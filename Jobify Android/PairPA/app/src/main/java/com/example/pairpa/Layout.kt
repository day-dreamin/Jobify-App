package com.example.pairpa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

//This composable function acts as the background
@Composable
fun bg(code: @Composable () -> Unit){
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
        code()
    }
}

//This composable function contains the catalogue made of a Lazy Vertical Staggered Grid containing clothing items
@Composable
fun MainLayout() {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
    ) {
        items(db.size) { id ->
            CardView(id)
        }
    }
}

//This function allows us to preview the Catalogue page
@Preview
@Composable
fun PreviewList() {
    popupvisible =  remember { mutableStateOf(false) }
    clickedid =  remember { mutableStateOf(0) }
    MaterialTheme {
        Surface {
            MainLayout()
        }
    }
}