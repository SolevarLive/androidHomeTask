package ru.urfu.chucknorrisdemo.data.repository

import kotlinx.coroutines.flow.first
import ru.urfu.chucknorrisdemo.data.api.ChuckApi
import ru.urfu.chucknorrisdemo.data.storage.JokeStorage
import ru.urfu.chucknorrisdemo.domain.model.JokeResult
import ru.urfu.chucknorrisdemo.domain.repository.IChuckRepository
import java.io.IOException

class ChuckRepository(
    private val api: ChuckApi,
    private val storage: JokeStorage
) : IChuckRepository {

    override suspend fun getCategories(): List<String> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getJoke(category: String?): JokeResult {
        return try {
            val response = if (category != null) {
                api.getJokeByCategory(category)
            } else {
                api.getRandomJoke()
            }

            if (response.isSuccessful) {
                val joke = response.body()?.value ?: "Неизвестная шутка"
                storage.saveJoke(joke)
                JokeResult.Success(joke)
            } else {
                val savedJoke = storage.getJoke().first()
                if (savedJoke.isNotEmpty()) {
                    JokeResult.Success("(Из кеша) $savedJoke")
                } else {
                    JokeResult.Error("Ошибка API: ${response.code()}")
                }
            }
        } catch (e: IOException) {
            val savedJoke = storage.getJoke().first()
            if (savedJoke.isNotEmpty()) {
                JokeResult.Success("(Оффлайн) $savedJoke")
            } else {
                JokeResult.Error("Нет подключения к интернету и нет сохранённых шуток")
            }
        } catch (e: Exception) {
            JokeResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }
}