package com.example.pairpa.Discussion

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.pairpa.PlaceHolder
import com.example.pairpa.R
import com.example.pairpa.auth
import com.example.pairpa.ctx
import com.example.pairpa.loggedIn
import com.example.pairpa.navController
import com.example.pairpa.navigateToHiddenTab
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.material3.Button as Button
import com.example.pairpa.Discussion.PostItem as PostItem

lateinit var posts: List<Post>

@Composable
fun PostItem(
    modifier: Modifier,
    post: Post,
    onPostClick: (Post) -> Unit
) {
    var ref = "profile_pictures/"
    ref += post.profile
    val storageRef = FirebaseStorage.getInstance().getReference(ref)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        val localFile = File.createTempFile("tempImage", ".jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
        }.addOnFailureListener {

        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onPostClick(post) }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "placeholder",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = post.title,
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = post.tags.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}


@Composable
fun CommentItem(comment: Comment) {
    var ref = "profile_pictures/"
    ref += comment.profile
    val storageRef = FirebaseStorage.getInstance().getReference(ref)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        val localFile = File.createTempFile("tempImage", ".jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
        }.addOnFailureListener {
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "placeholder",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = comment.email,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun PostDialog(
    originalPost: Post,
    onDismissRequest: () -> Unit,
    onAddComment: (String, Post) -> Unit,
    onCommentsUpdated: (Post) -> Unit
) {
    var commentText by remember { mutableStateOf("") }
    val post by remember(originalPost) { mutableStateOf(originalPost) }
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Expanded post") },
        text = {
            if (isLoading) {
                Text(text = "Loading...")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = post.title,
                        fontSize = 24.sp
                    )
                    Text(text = "By ${post.email} on ${post.date}")
                    Text(text = post.content)
                    val sortedComments = post.comments?.values?.sortedBy { it.timestamp }
                    if (sortedComments != null) {
                        sortedComments.forEach { comment ->
                            CommentItem(comment = comment)
                        }
                    }
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        label = { Text("Add a comment") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    onAddComment(commentText, post)
                    commentText = ""
                    onCommentsUpdated(post)
                    isLoading = false
                }
            ) {
                Text("Add Comment")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}


@Composable
fun DiscussionBoard(
    modifier: Modifier = Modifier
) {
    val viewModel: DiscussionBoardViewModel = viewModel()
    val posts by viewModel.posts.collectAsState(initial = emptyList())
    var isPostDialogOpen by remember { mutableStateOf(false) }
    val currentPost = remember { mutableStateOf<Post?>(null) }
    var isCreatePostDialogOpen by remember { mutableStateOf(false) }

    if (!loggedIn) {
        PlaceHolder()
    } else {
        LaunchedEffect(Unit) {
            viewModel.fetchPosts()
        }

        LaunchedEffect(posts) {
            viewModel.fetchPosts()
        }

        Column(
            modifier = modifier
                .padding(horizontal = 5.dp)
                .padding(vertical = 5.dp)
        ) {
            Button(
                onClick = { isCreatePostDialogOpen = true },
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Text(text = "Create Post")
            }
            LazyColumn {
                items(posts) { post ->
                    PostItem(
                        post = post,
                        onPostClick = {
                            isPostDialogOpen = true
                            currentPost.value = post
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .neu(
                                lightShadowColor = DefaultShadowColor.copy(0.1f),
                                darkShadowColor = DefaultShadowColor,
                                shadowElevation = 5.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Flat(RoundedCorner(8.dp)),
                            )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            if (isPostDialogOpen) {
                currentPost.value?.let { post ->
                    PostDialog(
                        originalPost = post,
                        onDismissRequest = {
                            isPostDialogOpen = false
                            currentPost.value = null
                        },
                        onAddComment = viewModel::addComment,
                        onCommentsUpdated = { updatedPost ->
                            viewModel.fetchPosts()
                            currentPost.value = updatedPost
                            isPostDialogOpen = false
                            navigateToHiddenTab("About")
                        }
                    )
                }
            }

            if (isCreatePostDialogOpen) {
                CreatePostDialog(
                    onDismissRequest = { isCreatePostDialogOpen = false },
                    onCreatePost = viewModel::createPost
                )
            }
        }
    }
}

@Composable
fun CreatePostDialog(
    onDismissRequest: () -> Unit,
    onCreatePost: (Post) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Create Post") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val post = Post(
                        email = auth.currentUser!!.email!!, // Replace with actual user email
                        date = getCurrentDateFormatted(), // Replace with actual date
                        title = title,
                        content = content,
                        comments = emptyMap(),
                        tags = tags.split(",").map { it.trim() },
                        profile = auth.currentUser?.uid.toString(),
                        timestamp = System.currentTimeMillis()
                    )
                    onCreatePost(post)
                    onDismissRequest()
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

val tempPost = Post()

class DiscussionBoardViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()

    private val database = FirebaseDatabase.getInstance().reference

    fun fetchPosts() {
        database.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                snapshot.children.forEach { child ->
                    val postId = child.key?: ""
                    val post = child.getValue(Post::class.java)?.copy(id = postId)
                    if (post!= null) {
                        listenForComments(postId, post) { updatedPost ->
                            val index = postList.indexOfFirst { it.id == postId }
                            if (index!= -1) {
                                postList[index] = updatedPost
                                _posts.value = postList.sortedByDescending { it.timestamp }
                            } else {
                                postList.add(updatedPost)
                                _posts.value = postList.sortedByDescending { it.timestamp }
                            }
                        }
                        postList.add(post)
                    }
                }
                _posts.value = postList.sortedByDescending { it.timestamp }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun listenForComments(postId: String, post: Post, onCommentsUpdated: (Post) -> Unit) {
        val commentsRef = database.child("posts").child(postId).child("comments")
        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = mutableMapOf<String, Comment>()
                snapshot.children.forEach { child ->
                    val comment = child.getValue(Comment::class.java)
                    if (comment != null) {
                        comments[child.key ?: ""] = comment
                    }
                }
                val updatedPost = post.copy(comments = comments)
                onCommentsUpdated(updatedPost)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun addComment(commentText: String, post: Post) {
        val currentTimestamp = System.currentTimeMillis()
        val comment = Comment(
            email = auth.currentUser!!.email!!, // Replace with actual user email
            content = commentText,
            profile = auth.currentUser!!.uid, // Replace with actual profile picture URL
            timestamp = currentTimestamp
        )

        val commentsRef = database.child("posts").child(post.id).child("comments")
        val newCommentRef = commentsRef.push()
        newCommentRef.setValue(comment)
    }

    fun createPost(post: Post) {
        val newPostRef = database.child("posts").push()
        newPostRef.setValue(post)
    }
}

fun getCurrentDateFormatted(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}