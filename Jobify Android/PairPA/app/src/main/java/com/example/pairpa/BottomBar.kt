package com.example.pairpa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

//This composable function add the Bottom Navigation Bar to the content
@Composable
fun generate(code: @Composable () -> Unit){
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomNavBar(
                navHostController = navController,
                items = getMenuBottomItems()
            )
        },
        content = {
            Box(modifier = Modifier.padding(it).fillMaxSize()){
                code()
            }
        }
    )
}

//This data class contains the data for a tab in the Bottom bar: The title, an icon, and the route
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
)

//This composable function contains the Bottom Navigation bar to allow the user to navigate across tabs through it
@Composable
fun BottomNavBar(
    navHostController: NavHostController,
    items: List<BottomNavigationItem>
) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ){
        items.forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route ==  bottomNavigationItem.route} == true,
                onClick = {
                    selectedIndex = index
                    navHostController.navigate(bottomNavigationItem.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navHostController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedIndex) {
                            bottomNavigationItem.selectedIcon
                        } else bottomNavigationItem.unselectedIcon,
                        contentDescription = bottomNavigationItem.title)
                },
                label = { Text(text = bottomNavigationItem.title) },
                alwaysShowLabel = true
            )
        }
    }
}