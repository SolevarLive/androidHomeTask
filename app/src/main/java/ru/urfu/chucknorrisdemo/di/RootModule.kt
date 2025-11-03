package ru.urfu.chucknorrisdemo.di

import org.koin.dsl.module
import ru.urfu.chucknorrisdemo.data.repository.ChuckRepository
import ru.urfu.chucknorrisdemo.data.storage.JokeStorage
import ru.urfu.chucknorrisdemo.domain.repository.IChuckRepository
import ru.urfu.chucknorrisdemo.presentation.viewModel.ChuckViewModel

val rootModule = module {
    single<JokeStorage> { JokeStorage(get()) }
    single<IChuckRepository> { ChuckRepository(get(), get()) }
    factory { ChuckViewModel(get()) }
}