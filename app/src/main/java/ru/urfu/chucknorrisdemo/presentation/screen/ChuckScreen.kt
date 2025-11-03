package ru.urfu.chucknorrisdemo.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.urfu.chucknorrisdemo.presentation.state.ChuckScreenState
import ru.urfu.chucknorrisdemo.presentation.viewModel.ChuckViewModel

@Composable
fun ChuckScreen(viewModel: ChuckViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val s = state) {
            is ChuckScreenState.Idle -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Загрузка категорий...")
            }

            ChuckScreenState.Loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Загрузка шутки...")
            }

            is ChuckScreenState.Error -> {
                Text("Ошибка: ${s.message}", color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.loadJoke() }) {
                    Text("Повторить")
                }
            }

            is ChuckScreenState.Success -> {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    Button(onClick = { expanded = true }) {
                        Text(s.selectedCategory ?: "Выбрать категорию")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Случайная") },
                            onClick = {
                                viewModel.loadJoke(null)
                                expanded = false
                            }
                        )
                        Divider()

                        s.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    viewModel.loadJoke(category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (s.joke.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = s.joke,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    Text("Выберите категорию для загрузки шутки")
                }

                Spacer(Modifier.height(16.dp))

                Button(onClick = { viewModel.loadJoke() }) {
                    Text("Случайная шутка")
                }
            }
        }
    }
}