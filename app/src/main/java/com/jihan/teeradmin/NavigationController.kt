package com.jihan.teeradmin

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jihan.teeradmin.domain.viewmodel.MatchViewmodel
import com.jihan.teeradmin.presentation.screens.ContactScreen
import com.jihan.teeradmin.presentation.screens.CreateMatchScreen
import com.jihan.teeradmin.presentation.screens.EditMatchScreen
import com.jihan.teeradmin.presentation.screens.GenerateLinkScreen
import com.jihan.teeradmin.presentation.screens.NotificationSenderScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationController() {
    val navController = rememberNavController()

    val matchViewmodel = koinViewModel<MatchViewmodel>()

    val navigateTo = remember { {route: Routes?->

        when(route){
            null -> navController.navigateUp()
            else -> navController.navigate(route)
        }

    } }



        NavHost(navController, Routes.MainRoute) {


            composable<Routes.MainRoute>(
                    enterTransition = { slideInHorizontally{it} },
                exitTransition = { slideOutHorizontally{it} }
            ) {
                MainScreen{navigateTo(it)}
            }


            composable<Routes.CreateMatchRoute>{
                CreateMatchScreen(onBack = {
                    navigateTo(null)
                })
            }

            composable<Routes.EditMatchRoute> {
                val route = it.toRoute<Routes.EditMatchRoute>()
                val matchDetail = matchViewmodel.getMatchDetailById(route.matchId)
                EditMatchScreen(
                    navigate = {navigateTo(it)},
                    matchDetail = matchDetail
                )
            }


            composable<Routes.ContactRoute> {
                ContactScreen()
            }

            composable<Routes.GenerateLinkRoutes>{
                GenerateLinkScreen()
            }

            composable<Routes.NotificationRoute> {
                NotificationSenderScreen()
            }

        }



}


sealed interface Routes {

    @Serializable
    data object MainRoute : Routes

    @Serializable
    data object CreateMatchRoute: Routes


    @Serializable
    data object ContactRoute: Routes

    @Serializable
    data class EditMatchRoute(
        val matchId: String
    ): Routes


    @Serializable
    data object GenerateLinkRoutes: Routes
    @Serializable
    data object NotificationRoute: Routes

}