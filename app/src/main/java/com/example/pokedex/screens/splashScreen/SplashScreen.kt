package com.example.pokedex.screens.splashScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pokedex.R
import com.example.pokedex.screens.Screens
import com.example.pokedex.ui.theme.Roboto
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    screenHeight: Dp,
    backgroundColor: Color
) {

    var isVisible by remember { mutableStateOf(false) }

    val composition by  rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash)
    )
    val speed by remember { mutableFloatStateOf(1.25f) }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = speed,
    )

    val textColor = if (isSystemInDarkTheme()) Color.Yellow else Color.Black


    LaunchedEffect(
        key1 = true,
        block = {
            isVisible = true
            delay(2000)
            navController.navigate(Screens.HomeScreen.route) {
                popUpTo(Screens.SplashScreen.route) { inclusive = true }
            }
        }
    )



    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            backgroundColor
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress =progress,
            modifier = Modifier.height(screenHeight*0.5f)
        )


        Spacer(modifier = Modifier.height(40.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(900, easing = EaseIn
                )
            ) + fadeIn(
                animationSpec = tween(900, easing = EaseOut
                )
            ),
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 28.sp,
                    color = textColor,
                    fontFamily = Roboto
                ),
            )
        }
    }

}

