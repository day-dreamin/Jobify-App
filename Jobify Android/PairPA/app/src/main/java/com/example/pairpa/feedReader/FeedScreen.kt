package com.example.pairpa.feedReader

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.pairpa.CollapsibleTb

@Composable
fun FeedScreen(
    posts: List<Post>,
    onPostClick: (Post) -> Unit,
    onCommunityClick: (Community) -> Unit
) {
    Column {
        posts.take(20).forEach { post ->
            PostItem(post, onPostClick)
            Divider()
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onCommunityClick(Community(0, "All Communities", "")) }) {
            Text("View Communities")
        }
    }
}

@Composable
fun PostItem(post: Post, onPostClick: (Post) -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onPostClick(post) }) {
        Column {
            Text(post.title, fontWeight = FontWeight.Bold)
            Text(post.content)
            // Show the number of comments or a preview
            Text("${post.comments.size} comments")
        }
    }
}

@Composable
fun PostDialog(post: Post, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(post.title) },
        text = {
            Column {
                post.content.split('\n').forEach { text ->
                    Text(text)
                }
                post.comments.forEach { comment ->
                    Text("${comment.user}: ${comment.content}")
                }
            }
        },
        confirmButton = {
            Button(onClick = { /* Add a comment */ }) {
                Text("Add Comment")
            }
        }
    )
}

@Preview
@Composable
fun PreviewTry() {
    MaterialTheme {
        Surface {
            FeedScreen(mutableListOf(), onPostClick = {}, onCommunityClick = {})
        }
    }
}

