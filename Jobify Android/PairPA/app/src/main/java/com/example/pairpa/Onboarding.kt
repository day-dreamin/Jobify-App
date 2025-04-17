package com.example.pairpa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Jobify!",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "This app serves as a job-seeking platform, along with having many other features!",
            style = MaterialTheme.typography.bodySmall
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Save a shared preference value to indicate that the user has dismissed the onboarding screen




                // Navigate to the main screen

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Skip")
        }
    }
}