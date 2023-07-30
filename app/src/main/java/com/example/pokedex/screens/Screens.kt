package com.example.pokedex.screens

sealed class Screens{
    object SplashScreen: Screens(){
        const val route = "splash_screen"
    }
    object HomeScreen: Screens(){
        const val route = "home_screen"
    }
    object DetailsScreen: Screens(){
        const val route = "details_screen"
    }
}
