package ru.urfu.chucknorrisdemo.domain.model

sealed class JokeResult {
    data class Success(val joke: String) : JokeResult()
    data class Error(val message: String) : JokeResult()
}