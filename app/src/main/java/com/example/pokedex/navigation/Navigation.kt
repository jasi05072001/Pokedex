package com.example.pokedex.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.screens.Screens
import com.example.pokedex.screens.homeScreen.HomeScreen
import com.example.pokedex.screens.splashScreen.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val backgroundColor = if (isSystemInDarkTheme())  Color(0xff1E293B) else Color(0xffBFDBFE)

    NavHost(
        navController = navController,
        startDestination =Screens.SplashScreen.route
    ){
        composable(Screens.SplashScreen.route){
            SplashScreen(
                navController = navController,
                screenHeight = screenHeight,
                backgroundColor = backgroundColor
            )
        }
        composable(route = Screens.HomeScreen.route){
            HomeScreen(
                navController = navController,
            )
        }
        composable(
            Screens.DetailsScreen.route+"/{dominantColor}/{pokemonName}",
            arguments =  listOf(
                navArgument("dominantColor"){
                    type = NavType.IntType
                },
                navArgument("pokemonName"){
                    type = NavType.StringType
                }
            )
        ) {
            val dominantColor = remember {
                val color = it.arguments?.getInt("dominantColor")
                color?.let { Color(it) } ?:Color.White
            }
            val pokemonName =  remember { it.arguments?.getString("pokemonName") }

        }

    }
}