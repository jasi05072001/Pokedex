package com.example.pokedex.screens.detailsScreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.pokedex.utils.parseStatToAbbr
import com.example.pokedex.utils.parseStatToColor
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
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 20.dp
                )
                .shadow(15.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xffececec))
                .align(Alignment.BottomCenter),
            navController = navController
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            if (pokemonDetails is Resources.Success){

                pokemonDetails.data?.sprites?.versions?.generationV?.blackWhite?.animated.let {
                    AsyncImage(
                        model = it?.frontDefault,
                        contentDescription = pokemonDetails.data?.name,
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .crossfade(true)
                            .crossfade(1500)
                            .build(),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(pokemonImgSize)
                            //.offset(y = topPadding)
                            .padding(bottom = 50.dp, top = 25.dp)

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
    navController: NavController
) {

    when(pokemonDetails){
        is Resources.Success -> {
            PokemonDetailsSection(
                pokemonDetails = pokemonDetails.data!!,
                modifier = modifier
                    .padding(30.dp),
                navController = navController
            )

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
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = pokemonDetails.name
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = Roboto,
            color = Color.Black
        )
        PokeMonTypeSection(types = pokemonDetails.types)
        PokemonDetailsDataSection(
            pokemonWeight = pokemonDetails.weight,
            pokemonHeight = pokemonDetails.height
        )

        PokemonBaseStats(pokemonDetails = pokemonDetails)

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                pokemonDetails.id?.let {

                    navController.navigate("pokemon_details_screen/${it}")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "See more")
        }


    }
}

@Composable
fun PokeMonTypeSection(
    types: List<Type>
) {
    Row(

        modifier = Modifier.padding(16.dp)
    )
    {
        for(type in types){
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

    }

}


@Composable
fun PokemonDetailsDataSection(
    pokemonWeight :Int,
    pokemonHeight :Int,
    sectionHeight: Dp = 100.dp,
) {
    val pokemonWeightInKg = remember {
        pokemonWeight / 10f
    }
    val pokemonHeightInMeters = remember {
        pokemonHeight / 10f
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PokemonDetailsDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg" ,
            dataIcon = painterResource(id = R.drawable.weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.Black.copy(alpha = 0.2f)))

        PokemonDetailsDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m" ,
            dataIcon = painterResource(id = R.drawable.height),
            modifier = Modifier.weight(1f)
        )

    }


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
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null , tint = Color.Black, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "$dataValue $dataUnit",
            color = Color.Black,
            fontSize = 18.sp,
            fontFamily = Roboto
        )

    }

}

@Composable
fun PokemonStats(
    statName :String,
    statValue :Int,
    statMaxValue: Int,
    statColor :Color,
    statHeight :Dp = 30.dp,
    animDuration :Int = 1000,
    animDelay :Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay,
            easing = FastOutSlowInEasing
        ), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(statHeight)
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(currentPercent.value)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = statName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = Roboto
            )
            Text(
                text = (currentPercent.value * statMaxValue).toInt().toString(),
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = Roboto
            )
        }
    }

}

@Composable
fun PokemonBaseStats (
    pokemonDetails: Pokemon,
    animDelayPerItem :Int = 150,

    ) {
    val maxBaseStat = remember {
        pokemonDetails.stats.maxOf { it.baseStat }
    }

    Column(Modifier.fillMaxWidth()) {

        Text(
            text = "Base Stats",
            color = Color.Black,
            fontSize = 20.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        for(i in pokemonDetails.stats.indices){
            val stat = pokemonDetails.stats[i]
            PokemonStats(
                statName = parseStatToAbbr(stat),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

    }

}

