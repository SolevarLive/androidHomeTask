package ru.urfu.chucknorrisdemo.domain.repository

import ru.urfu.chucknorrisdemo.domain.model.JokeResult

interface IChuckRepository {
    suspend fun getCategories(): List<String>
    suspend fun getJoke(category: String? = null): JokeResult
}