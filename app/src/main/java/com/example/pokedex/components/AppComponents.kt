package com.example.pokedex.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.ui.theme.Roboto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    hint: String,
    value :String,
    onValueChange: (String) -> Unit
) {


    val containerColor = Color(0xff073b4c).copy(alpha = 0.5f)

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
            containerColor = containerColor,
            cursorColor = textColor,
        ),
        modifier = Modifier
            .padding(end = 20.dp, top = 2.dp, bottom = 2.dp)
            .height(50.dp)
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







