package com.example.pairpa

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pairpa.Chat.ChatScreen
import com.example.pairpa.Discussion.DiscussionBoard
import com.example.pairpa.Job.JobListingsScreen
import com.example.pairpa.ui.theme.PairPATheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController
lateinit var isDarkTheme: MutableState<Boolean>
var backGround = mutableStateOf(R.drawable.light_theme)
@SuppressLint("StaticFieldLeak")
lateinit var ctx: Context
lateinit var auth: FirebaseAuth
lateinit var database: FirebaseDatabase

//This function allows the Bottom Navigation bar to be filled with the Tab icons in the Bottom bar
fun getMenuBottomItems() = mutableListOf<BottomNavigationItem>(
    BottomNavigationItem(
        title = "Listings",
        selectedIcon = Icons.Outlined.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = "main"
    ),
    BottomNavigationItem(
        title = "Discussion",
        selectedIcon = Icons.Outlined.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        route = "info"
    ),
    BottomNavigationItem(
        title = "Chat",
        selectedIcon = Icons.Outlined.Send,
        unselectedIcon = Icons.Outlined.Send,
        route = "comment"
    ),
    BottomNavigationItem(
        title = "Account",
        selectedIcon = Icons.Outlined.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        route = "login"
    ),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        ctx = applicationContext
        database = Firebase.database
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        super.onCreate(savedInstanceState)
        setContent {
            val dt = isSystemInDarkTheme()
            backGround = remember { mutableStateOf(R.drawable.light_theme) }
            isDarkTheme = remember { mutableStateOf(dt) }
            PairPATheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }
}

//This composable contains the Navigation controller allowing the user to navigate between tabs, starting at the "main" tab
/*@Composable
fun Main() {
    popupvisible =  remember { mutableStateOf(false) }
    clickedid =  remember { mutableStateOf(0) }
    navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            generate{
                CollapsibleTb("Blossom Boutique",{
                    bg{
                        JobListingsScreen(
                            onJobListingClick = { jobListing ->
                                Toast.makeText(
                                    ctx,
                                    "Clicked on ${jobListing.title} at ${jobListing.company}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            filterText = emptyList(),
                            modifier = Modifier.fillMaxSize()
                        )
                        //MainLayout()
                        DialogWithImage(
                            id = clickedid,
                            visible = popupvisible
                        )
                    }
                })
            }
        }
        composable("info"){
            generate {
                CollapsibleTb("About us", {
                    bg {
                        //ScrollableAbout()
                        DiscussionBoard()
                    }
                })
            }
        }
        composable("comment"){
            generate{
                CollapsibleTb("Reviews", {
                    bg {
                        //ReviewLayout()
                        ChatScreen()
                    }
                })
            }
        }
        composable("login"){
            generate {
                CollapsibleTb("Account", {
                    bg {
                        ScrollableLoginBox()
                    }
                })
            }
        }
        composable("cart"){
            generate{
                CollapsibleTb("Shopping Cart", {
                    bg {
                        CartLayout()
                    }
                })
            }
        }
    }
}*/

var coroutineScope: CoroutineScope? = null
/*@Composable
fun Main() {
    var popupvisible = remember { mutableStateOf(false) }
    var clickedid = remember { mutableStateOf(0) }
    navController = rememberNavController()
    coroutineScope = rememberCoroutineScope()

        // Display the main screen
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            generate {
                CollapsibleTb("Jobify", {
                    bg {
                        JobListingsScreen(
                            onJobListingClick = { jobListing -> },
                            filterText = emptyList(),
                            modifier = Modifier.fillMaxSize()
                        )
                        //MainLayout()
                        DialogWithImage(
                            id = clickedid,
                            visible = popupvisible
                        )
                    }
                })
            }
        }
        composable("info") {
            generate {
                CollapsibleTb("Discussion", {
                    bg {
                        //ScrollableAbout()
                        DiscussionBoard()
                    }
                })
            }
        }
        composable("comment") {
            generate {
                CollapsibleTb("Chat", {
                    bg {
                        //ReviewLayout()
                        ChatScreen()
                    }
                })
            }
        }
        composable("login") {
            generate {
                CollapsibleTb("Account", {
                    bg {
                        ScrollableLoginBox()
                    }
                })
            }
        }
        composable("cart") {
            generate {
                CollapsibleTb("Shopping Cart", {
                    bg {
                        CartLayout()
                    }
                })
            }
        }
        // Add a hidden tab without a route
        composable("hidden") {
            PlaceHolder3()
        }
    }

    // Define a global no-parameter method to navigate to the hidden tab and return after 1 second
}*/

@Composable
fun Main() {
    var popupvisible = remember { mutableStateOf(false) }
    var clickedid = remember { mutableStateOf(0) }
    navController = rememberNavController()
    coroutineScope = rememberCoroutineScope()

    // Check if the user has completed the onboarding
    val sharedPreferences = ctx.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

    // Set the starting destination based on onboarding completion
    val startDestination = if (onboardingCompleted) "main" else "onboarding"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(
                onOnboardingCompleted = {
                    // Update the shared preferences to mark onboarding as completed
                    sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
                    // Navigate to the main screen
                    navController.navigate("main") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            generate {
                CollapsibleTb("Jobify", {
                    bg {
                        JobListingsScreen(
                            onJobListingClick = { jobListing -> },
                            filterText = emptyList(),
                            modifier = Modifier.fillMaxSize()
                        )
                        //MainLayout()
                        DialogWithImage(
                            id = clickedid,
                            visible = popupvisible
                        )
                    }
                })
            }
        }
        composable("info") {
            generate {
                CollapsibleTb("Discussion", {
                    bg {
                        //ScrollableAbout()
                        DiscussionBoard()
                    }
                })
            }
        }
        composable("comment") {
            generate {
                CollapsibleTb("Chat", {
                    bg {
                        //ReviewLayout()
                        ChatScreen()
                    }
                })
            }
        }
        composable("login") {
            generate {
                CollapsibleTb("Account", {
                    bg {
                        ScrollableLoginBox()
                    }
                })
            }
        }
        composable("cart") {
            generate {
                CollapsibleTb("Shopping Cart", {
                    bg {
                        CartLayout()
                    }
                })
            }
        }
        // Add a hidden tab without a route
        composable("hidden") {
            PlaceHolder3()
        }
        // ... (rest of your composables)
    }

    // Define a global no-parameter method to navigate to the hidden tab and return after 1 second
}

@Composable
fun OnboardingScreen(onOnboardingCompleted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Jobify!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.baseline_diversity_1_24),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Discover your dream job with Jobify!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Browse through a wide range of job listings, connect with other professionals, and unlock exciting career opportunities.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onOnboardingCompleted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Get Started")
        }
    }
}


fun navigateToHiddenTab(currentDestinationId: String) {
    coroutineScope?.launch {
        navController?.navigate("hidden") {
            popUpTo(currentDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
        }
        delay(1000)
        navController?.popBackStack()
    }
}