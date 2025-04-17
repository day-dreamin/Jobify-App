package com.example.pairpa.Job

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material.Chip
import com.example.pairpa.ctx
import com.example.pairpa.database
import com.example.pairpa.navigateToHiddenTab
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.time.delay

val test = JobListing(
    "This should never be called",
    "Acme Inc.",
    "New York, NY",
    listOf("Android", "Kotlin", "Java"),
    "none"
)
val jobListings: MutableList<JobListing> = mutableStateListOf(test)
lateinit var databaseRef: DatabaseReference

fun fetchJobListings(onSuccess: () -> Unit, onError: () -> Unit) {
    databaseRef = database.getReference("jobListings")
    databaseRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            jobListings.clear()
            for (dataSnapshot in snapshot.children) {
                val jobListing = dataSnapshot.getValue(JobListing::class.java)
                if (jobListing != null) {
                    jobListings.add(jobListing)
                }
            }
            jobListings.reverse()
            onSuccess()
        }

        override fun onCancelled(error: DatabaseError) {
            onError()
        }
    })
}

/*@Composable
fun AddJobListingDialog(
    showDialog: MutableState<Boolean>,
    onJobListingAdded: () -> Unit
) {
    val titleState = remember { mutableStateOf("") }
    val companyState = remember { mutableStateOf("") }
    val locationState = remember { mutableStateOf("") }
    val tagsState = remember { mutableStateOf("") }
    val detailState = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Add Job Listing") },
            text = {
                Column {
                    OutlinedTextField(
                        value = titleState.value,
                        onValueChange = { titleState.value = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = companyState.value,
                        onValueChange = { companyState.value = it },
                        label = { Text("Company") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = locationState.value,
                        onValueChange = { locationState.value = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tagsState.value,
                        onValueChange = { tagsState.value = it },
                        label = { Text("Tags (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = detailState.value,
                        onValueChange = { detailState.value = it },
                        label = { Text("Detail") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val tags = tagsState.value.split(",").map { it.trim() }
                        val timestamp = System.currentTimeMillis()
                        val jobListing = JobListing(
                            titleState.value,
                            companyState.value,
                            locationState.value,
                            tags,
                            detailState.value,
                            timestamp
                        )
                        titleState.value = ""
                        companyState.value = ""
                        locationState.value = ""
                        tagsState.value = ""
                        detailState.value = ""
                        databaseRef.push().setValue(jobListing)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showDialog.value = false
                                    onJobListingAdded()
                                } else {
                                    // Handle error
                                }
                            }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}*/

@Composable
fun AddJobListingDialog(
    showDialog: MutableState<Boolean>,
    onJobListingAdded: () -> Unit
) {
    val titleState = remember { mutableStateOf("") }
    val companyState = remember { mutableStateOf("") }
    val locationState = remember { mutableStateOf("") }
    val tagsState = remember { mutableStateOf("") }
    val detailState = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Add Job Listing") },
            text = {
                Column {
                    OutlinedTextField(
                        value = titleState.value,
                        onValueChange = { titleState.value = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = companyState.value,
                        onValueChange = { companyState.value = it },
                        label = { Text("Company") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = locationState.value,
                        onValueChange = { locationState.value = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tagsState.value,
                        onValueChange = { tagsState.value = it },
                        label = { Text("Tags (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = detailState.value,
                        onValueChange = { detailState.value = it },
                        label = { Text("Detail") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val tags = tagsState.value.split(",").map { it.trim() }
                        val timestamp = System.currentTimeMillis()
                        val jobListing = JobListing(
                            titleState.value,
                            companyState.value,
                            locationState.value,
                            tags,
                            detailState.value,
                            timestamp
                        )
                        titleState.value = ""
                        companyState.value = ""
                        locationState.value = ""
                        tagsState.value = ""
                        detailState.value = ""
                        databaseRef.push().setValue(jobListing)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showDialog.value = false
                                    onJobListingAdded()
                                    navigateToHiddenTab("About")
                                } else {
                                    // Handle error
                                }
                            }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

/*@Composable
fun JobListingsScreen(
    onJobListingClick: (JobListing) -> Unit,
    filterText: List<String>,
    modifier: Modifier = Modifier
) {

    val showAddDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val filteredJobListings = remember(searchText, filterText) {
        jobListings.filter { jobListing ->
            jobListing.title.contains(searchText, ignoreCase = true) ||
                    jobListing.company.contains(searchText, ignoreCase = true) ||
                    jobListing.location.contains(searchText, ignoreCase = true) ||
                    jobListing.tags.any { it.contains(searchText, ignoreCase = true) }
        }.filter { jobListing ->
            filterText.all { filter ->
                jobListing.tags.contains(filter)
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchJobListings(onSuccess = {
            isLoading.value = false },
            onError = { /* Handle error */ })
        //isLoading.value = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { setSearchText(it) },
            label = {
                Text(
                    text = "Search job listings",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { showAddDialog.value = true },
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Text("Add Job Listing")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading.value) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        } else {
            if (filteredJobListings.size == 1 && filteredJobListings[0].title == "This should never be called") {
                JobListings(
                    jobListings = jobListings,
                    onJobListingClick = onJobListingClick,
                    modifier = Modifier
                )
            }
            else {
                JobListings(
                    jobListings = filteredJobListings,
                    onJobListingClick = onJobListingClick,
                    modifier = Modifier
                )
            }
        }
    }

    AddJobListingDialog(showAddDialog) {
        fetchJobListings(onSuccess = { isLoading.value = false },
            onError = { /* Handle error */ })
    }
}*/

@Composable
fun JobListingsScreen(
    onJobListingClick: (JobListing) -> Unit,
    filterText: List<String>,
    modifier: Modifier = Modifier
) {
    val showAddDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val filteredJobListings = remember(searchText, filterText) {
        jobListings.filter { jobListing ->
            jobListing.title.contains(searchText, ignoreCase = true) ||
                    jobListing.company.contains(searchText, ignoreCase = true) ||
                    jobListing.location.contains(searchText, ignoreCase = true) ||
                    jobListing.tags.any { it.contains(searchText, ignoreCase = true) }
        }.filter { jobListing ->
            filterText.all { filter ->
                jobListing.tags.contains(filter)
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchJobListings(
            onSuccess = {
                //navigateToHiddenTab("Products")
                isLoading.value = false
            },
            onError = { /* Handle error */ }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { setSearchText(it) },
            label = {
                Text(
                    text = "Search job listings",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                showAddDialog.value = true
                isLoading.value = true },
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Text("Add Job Listing")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading.value) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        } else {
            if (filteredJobListings.size == 1 && filteredJobListings[0].title == "This should never be called") {
                JobListings(
                    jobListings = jobListings,
                    onJobListingClick = onJobListingClick,
                    modifier = Modifier
                )
            } else {
                JobListings(
                    jobListings = filteredJobListings,
                    onJobListingClick = onJobListingClick,
                    modifier = Modifier
                )
            }
        }
    }

    AddJobListingDialog(showAddDialog) {
        fetchJobListings(
            onSuccess = {
                isLoading.value = false
            },
            onError = { /* Handle error */ }
        )
    }
}

@Composable
fun JobListingItem(
    jobListing: JobListing,
    onJobListingClick: (JobListing) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDetailsDialog = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                showDetailsDialog.value = true
                onJobListingClick(jobListing)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp, 16.dp, 8.dp)

            ) {
                Text(
                    text = jobListing.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = jobListing.company,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 12.dp)
            ) {
                Text(
                    text = jobListing.location,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(1),
                modifier = Modifier.weight(1f),
                content = {
                    /*items(jobListing.tags.size) { index ->
                        Chip(
                            label = { Text(text = jobListing.tags[index]) },
                            onClick = { /* Do nothing */ }
                        )
                    }*/
                    items(jobListing.tags.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.LightGray)
                        ) {
                            Text(
                                text = jobListing.tags[index],
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                            )
                        }
                    }
                }
            )
        }

        if (showDetailsDialog.value) {
            Dialog(
                onDismissRequest = {
                    showDetailsDialog.value = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(width = 350.dp, height = 500.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp))
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = jobListing.title,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${jobListing.company}, ${jobListing.location}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    var str: String = ""
                    jobListing.tags.forEach { tag ->
                        str = str + tag
                        str = str + "; "
                    }
                    Text(
                        text = str,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = jobListing.detail,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            showDetailsDialog.value = false
                        }
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun JobListings(
    jobListings: List<JobListing>,
    onJobListingClick: (JobListing) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        jobListings.forEach { jobListing ->
            JobListingItem(
                jobListing = jobListing,
                onJobListingClick = onJobListingClick,
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
}
