package ru.urfu.chucknorrisdemo.presentation.state

sealed interface ChuckScreenState {
    object Idle : ChuckScreenState
    object Loading : ChuckScreenState
    data class Success(val joke: String, val categories: List<String>, val selectedCategory: String?) : ChuckScreenState
    data class Error(val message: String) : ChuckScreenState
}