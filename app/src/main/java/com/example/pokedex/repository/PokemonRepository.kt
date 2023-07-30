package com.example.pokedex.repository

import com.example.pokedex.data.PokeApi
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.data.remote.responses.PokemonList
import com.example.pokedex.utils.Resources
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {

    suspend fun  getPokemonList(limit: Int, offset: Int) : Resources<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resources.Error("An unknown error occured.")
        }
        return Resources.Success(response)
    }

    suspend fun  getPokemonDetails(pokemonName:String) : Resources<Pokemon> {
        val response = try {
            api.getPokemonDetails(pokemonName)
        } catch (e: Exception) {
            return Resources.Error("An unknown error occured.")
        }
        return Resources.Success(response)
    }
}