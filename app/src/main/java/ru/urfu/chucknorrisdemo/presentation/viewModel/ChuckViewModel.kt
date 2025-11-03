package ru.urfu.chucknorrisdemo.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.urfu.chucknorrisdemo.domain.model.JokeResult
import ru.urfu.chucknorrisdemo.domain.repository.IChuckRepository
import ru.urfu.chucknorrisdemo.presentation.state.ChuckScreenState

class ChuckViewModel(
    private val repository: IChuckRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ChuckScreenState>(ChuckScreenState.Idle)
    val state: StateFlow<ChuckScreenState> = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()
                _state.value = ChuckScreenState.Success(
                    joke = "",
                    categories = categories,
                    selectedCategory = null
                )
            } catch (e: Exception) {
                _state.value = ChuckScreenState.Success(
                    joke = "",
                    categories = emptyList(),
                    selectedCategory = null
                )
            }
        }
    }

    fun loadJoke(category: String? = null) {
        _state.value = ChuckScreenState.Loading
        viewModelScope.launch {
            val result = repository.getJoke(category)
            val currentCategories = when (val currentState = _state.value) {
                is ChuckScreenState.Success -> currentState.categories
                else -> emptyList()
            }

            when (result) {
                is JokeResult.Success -> {
                    _state.value = ChuckScreenState.Success(
                        joke = result.joke,
                        categories = currentCategories,
                        selectedCategory = category
                    )
                }
                is JokeResult.Error -> {
                    _state.value = ChuckScreenState.Error(result.message)
                }
            }
        }
    }
}