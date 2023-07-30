package com.example.pokedex.screens.detailsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.pokedex.R
import com.example.pokedex.components.AppLoader
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.data.remote.responses.Type
import com.example.pokedex.ui.theme.Roboto
import com.example.pokedex.utils.Resources
import com.example.pokedex.utils.parseTypeToColor
import com.example.pokedex.viewModels.PokemonDetails
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImgSize: Dp = 200.dp,
    viewModel: PokemonDetails = hiltViewModel()
) {
    val pokemonDetails = produceState<Resources<Pokemon>>(initialValue = Resources.Loading()){
        value =viewModel.getPokemonInfo(pokemonName = pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {

        PokeMonDetailsTop(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .align(Alignment.TopCenter)
        )

        PokeMonDetailsStateWrapper(
            pokemonDetails =pokemonDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImgSize / 2f,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                )
                .shadow(15.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xffececec))
                .padding(14.dp)
                .align(Alignment.BottomCenter)
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            if (pokemonDetails is Resources.Success){

                pokemonDetails.data?.sprites?.let {
                    AsyncImage(
                        model = it.frontDefault,
                        contentDescription = pokemonDetails.data.name,
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .crossfade(true)
                            .crossfade(1500)
                            .build(),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(pokemonImgSize)
                            .offset(y = topPadding)

                    )
                }
            }
        }
    }
}

@Composable
fun PokeMonDetailsTop(
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color.Transparent)
                )
            )
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()

            }
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier

            )
        }
    }
}

@Composable
fun PokeMonDetailsStateWrapper(
    pokemonDetails: Resources<Pokemon>,
    modifier: Modifier = Modifier,
) {

    when(pokemonDetails){
        is Resources.Success -> {

        }
        is  Resources.Error -> {

            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_erro),
                    contentDescription = "error",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pokemonDetails.message!!,
                    color = Color.Red,
                    fontSize = 22.sp,
                    fontFamily = Roboto,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

            }

        }
        is Resources.Loading -> {
            AppLoader()
        }
    }

}

@Composable
fun PokemonDetailsSection(
    pokemonDetails: Pokemon,
    modifier: Modifier = Modifier,

    ) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .offset(100.dp)
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = pokemonDetails.id.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = Roboto,
            color = Color.Black
        )

    }
}

@Composable
fun PokeMonTypeSection(
    types: List<Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp))
    {
        for(type in types){
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(parseTypeToColor(type))
                    .padding(8.dp)
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }

}


//TODO:11:55
@Composable
fun PokemonDetailsDataSection() {


}

@Composable
fun PokemonDetailsDataItem(
    dataValue :Float,
    dataUnit :String,
    dataIcon :Painter,
    modifier: Modifier = Modifier

) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null , tint = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "$dataValue $dataUnit",
            color = Color.Black,
            fontSize = 18.sp,
            fontFamily = Roboto
        )

    }

}

