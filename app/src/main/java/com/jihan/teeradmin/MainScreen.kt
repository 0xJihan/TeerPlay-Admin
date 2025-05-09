package com.jihan.teeradmin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.House
import com.composables.icons.lucide.LayoutDashboard
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import com.jihan.teeradmin.presentation.screens.HomeScreen
import com.jihan.composeutils.CenterBox
import com.jihan.teeradmin.presentation.screens.AdminDashboard


@Composable
fun MainScreen(navigate: (Routes) -> Unit) {

    val list = remember {
        listOf(
            NavItem("Home", Lucide.House, Screen.Home),
            NavItem("Dashboard", Lucide.LayoutDashboard, Screen.Dashboard),
        )
    }

    var selectedPosition by rememberSaveable { mutableIntStateOf(0) }


    Scaffold(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background), bottomBar = {
        BottomAppBar {
            list.forEachIndexed { index, navItem ->
                NavigationBarItem(
                    selected = selectedPosition == index,
                    onClick = {
                        selectedPosition = index
                    },
                    icon = {
                        Icon(navItem.icon, contentDescription = navItem.title)
                    },
                    label = {
                        Text(navItem.title)
                    }
                )
            }
        }
    }) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(targetState = selectedPosition, transitionSpec = {
                // Customizing the animation with slide and fade effects
                if (targetState > initialState) {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> width } + fadeOut())
                }
            }, label = "") { index ->
                when (list[index].screen) {
                    Screen.Home -> {
                        HomeScreen { navigate(it) }
                    }

                    Screen.Dashboard -> AdminDashboard{navigate(it)}
                }
            }
        }

    }

}


data class NavItem(val title: String, val icon: ImageVector, val screen: Screen)

enum class Screen {
    Home, Dashboard
}