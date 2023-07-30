@file:Suppress("DEPRECATION")

package com.example.pokedex.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pokedex.R
import com.example.pokedex.ui.theme.Roboto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    hint: String,
    value :String,
    onValueChange: (String) -> Unit
) {

    val textColor = Color.White

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange.invoke(it)
        },
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text(
                text = hint,
                color = textColor,
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            containerColor = Color.DarkGray,
            cursorColor = textColor,
        ),
        modifier = Modifier
            .padding(start = 30.dp, bottom = 2.dp, top = 2.dp, end = 10.dp)
            .height(48.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = textColor,
            letterSpacing = 0.15.sp,
            lineHeight = 18.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Search,
            autoCorrect = false,
        ),

        )

}

@Composable
fun AppLoader() {
    val composition by  rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loader)
    )
    val speed by remember { mutableFloatStateOf(1.25f) }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = speed,
    )
    Dialog(
        onDismissRequest = {  },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .size(100.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress =progress,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentScale = ContentScale.FillBounds
            )

        }
    }
}







