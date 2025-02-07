package com.gabriel.pokedexapp.features.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.gabriel.pokedexapp.data.models.PokedexListEntry
import com.gabriel.pokedexapp.repository.PokemonRepository
import com.gabriel.pokedexapp.util.Constants.PAGE_SIZE
import com.gabriel.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0
    private val _pokemonList = mutableStateOf<List<PokedexListEntry>>(emptyList())
    val pokemonList: State<List<PokedexListEntry>> = _pokemonList

    private val _loadError = mutableStateOf("")
    val loadError: State<String> = _loadError

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _endReached = mutableStateOf(false)
    val endReached: State<Boolean> = _endReached

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true
    private val _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> = _isSearching

    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) _pokemonList.value else cachedPokemonList
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                _pokemonList.value = cachedPokemonList
                _isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = _pokemonList.value
                isSearchStarting = false
            }
            _pokemonList.value = results
            _isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    _endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { _, entry ->
                        val number = extractPokemonNumber(entry.url)
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    curPage++
                    _loadError.value = ""
                    _isLoading.value = false
                    _pokemonList.value += pokedexEntries
                }

                is Resource.Error -> {
                    _loadError.value = result.message ?: "Unknown error"
                    _isLoading.value = false
                }

                else -> Unit
            }
        }
    }

    private fun extractPokemonNumber(url: String): String {
        return if (url.endsWith("/")) {
            url.dropLast(1).takeLastWhile { it.isDigit() }
        } else {
            url.takeLastWhile { it.isDigit() }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}