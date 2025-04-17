package com.example.pairpa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

//This composable function contains the Collapsible Tool bar at the top of the app
//It also contains the Overflow Menu with the option to toggle light and dark mode inside
@Composable
fun CollapsibleTb(name: String, code: @Composable () -> Unit,
                  bonusmenu: @Composable (MutableState<Boolean>) -> Unit = {}){
    val state = rememberCollapsingToolbarScaffoldState()
    var showMenu: MutableState<Boolean> =  remember { mutableStateOf(false) }
    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = state,
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {

            val textSize = (18 + 18 * state.toolbarState.progress).sp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .pin()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )

            Text(
                name,
                style = TextStyle(fontSize = textSize),
                modifier = Modifier
                    .padding(16.dp)
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomStart)
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()) {
                Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                    IconButton(
                        onClick = { showMenu.value = !showMenu.value },
                    ) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false },
                    ) {
                        val view = LocalView.current
                        val backgroundState = rememberUpdatedState(backGround)
                        DropdownMenuItem({
                            if(isDarkTheme.value) {
                                Text("Light mode")
                            }else{
                                Text("Dark mode")
                            }
                        }, onClick = {
                            showMenu.value = false
                            if (backGround.value == R.drawable.light_theme) {
                                backGround.value = R.drawable.dark_theme
                            }
                            else if (backGround.value == R.drawable.dark_theme) {
                                backGround.value = R.drawable.light_theme
                            }

                            isDarkTheme.value = !isDarkTheme.value
                            view.invalidate()
                        })
                    }
                }
            }
        }
    ){
        code()
    }
}

//This function allows us to preview the Collapsible Tool bar
@Preview
@Composable
fun PreviewCollapse() {
    MaterialTheme {
        Surface {
            CollapsibleTb("lorem",{})
        }
    }
}