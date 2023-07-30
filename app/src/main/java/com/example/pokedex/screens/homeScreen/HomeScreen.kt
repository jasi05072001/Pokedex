package com.example.pokedex.screens.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pokedex.R
import com.example.pokedex.components.SearchBar
import com.example.pokedex.data.models.PokeDexListEntry
import com.example.pokedex.screens.Screens
import com.example.pokedex.ui.theme.Roboto
import com.example.pokedex.viewModels.PokemonListViewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
) {


    val isSearchBarVisible = remember {
        mutableStateOf(false)
    }
    val searchBarAnimationProgress = remember {
        mutableStateOf(false)
    }
    var name by remember { mutableStateOf("") }
    Surface(
        color = Color(0xff1E293B),
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(modifier = Modifier.height(54.dp),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        if (isSearchBarVisible.value) {
                            SearchBar(
                                hint = "Search Pokemon",
                                value = name,
                                onValueChange = {
                                    name = it
                                }
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_pokemon_logo),
                                contentDescription = "Pokemon Logo",
                                modifier = Modifier
                                    .height(50.dp)
                                    .padding(4.dp)
                            )
                        }
                    },
                    actions = {
                        if (!isSearchBarVisible.value) {
                            AnimatedVisibility(
                                visible = !isSearchBarVisible.value,
                                enter = slideInVertically(
                                    initialOffsetY = { fullWidth -> fullWidth },
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        easing = EaseInOut
                                    )
                                ),
                            ) {
                                IconButton(
                                    onClick = {
                                        isSearchBarVisible.value = !isSearchBarVisible.value
                                        searchBarAnimationProgress.value =
                                            !searchBarAnimationProgress.value
                                    }) {
                                    Image(
                                        imageVector = Icons.TwoTone.Search,
                                        contentDescription = "Search",
                                        colorFilter = ColorFilter.tint(
                                            Color.White
                                        )
                                    )
                                }
                            }
                        }
                        else {
                            AnimatedVisibility(
                                visible = isSearchBarVisible.value,
                                enter = slideInVertically(
                                    initialOffsetY = { fullWidth -> fullWidth },
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        easing = EaseInOut
                                    )
                                ),
                            ) {
                                IconButton(
                                    onClick = {
                                        name = ""

                                    }
                                ) {
                                    Image(
                                        imageVector = Icons.Outlined.Cancel,
                                        contentDescription = "Close Search",
                                        colorFilter = ColorFilter.tint(
                                            Color.White
                                        )
                                    )
                                }

                            }
                        }
                    },
                    navigationIcon = {
                        if (isSearchBarVisible.value) {
                            IconButton(
                                onClick = {
                                    isSearchBarVisible.value = !isSearchBarVisible.value
                                }
                            ) {
                                Image(
                                    imageVector = Icons.Outlined.ArrowBackIosNew,
                                    contentDescription = "Close Search",
                                    Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(
                                        Color.White
                                    )
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xff1E293B)
                    )
                )


            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)
                    .background(Color(0xff1E293B))
            ) {

                PokemonList(
                    navController = navController,
                )
            }

        }
    }

}

@Preview
@Composable
fun PreviewHomeScreen() {
    val context = LocalContext.current
    HomeScreen( navController = NavController(context))
}


@Composable
fun PokeMonCard(
    entry: PokeDexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val defaultDominantColor = Color(0xffececec)

    val context = LocalContext.current

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
        modifier= modifier
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor

                    )
                )
            )
            .clickable {
                navController.navigate(
                    Screens.DetailsScreen.route + "/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            },
    ) {
        Column {
            AsyncImage(
                model = entry.imageUrl,
                contentDescription = entry.pokemonName,
                imageLoader = ImageLoader.Builder(context)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .size(100.dp)
                    .align(CenterHorizontally),
                contentScale = ContentScale.FillBounds,
                onSuccess = {
                    viewModel.getDominantColor(it.result.drawable) { color -> dominantColor = color }

                },


                )

            Text(text = entry.pokemonName,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Roboto,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}

@Composable
fun PokeDexRow(
    index: Int,
    entries : List<PokeDexListEntry>,
    navController: NavController,
) {


    Column (modifier = Modifier.padding(10.dp)){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            PokeMonCard(
                entry = entries[index* 2],
                navController =navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp) )
            if (entries.size >= index *2 +2){
                PokeMonCard(
                    entry = entries[index* 2 +1],
                    navController =navController,
                    modifier = Modifier.weight(1f)
                )
            }
            else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel =  hiltViewModel()
){
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }

    val composition by  rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loader)
    )
    val speed by remember { mutableFloatStateOf(1.25f) }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = speed,
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ){
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            (pokemonList.size / 2) + 1
        }
        items(itemCount){
            if (it >= itemCount -1 && !endReached){
                viewModel.loadPokemon()
            }
            PokeDexRow(index = it, entries = pokemonList, navController = navController)
        }
    }
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading){
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
                    contentAlignment = Center
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
        if (loadError.isNotEmpty()){
            RetrySection(error = loadError.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }) {
                viewModel.loadPokemon()

            }
        }
    }
}


@Composable
fun RetrySection(
    error :String,
    onRetry:()-> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = CenterHorizontally,
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
            text = error,
            color = Color.Red,
            fontSize = 22.sp,
            fontFamily = Roboto,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry.invoke() },
            modifier =  Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }

}