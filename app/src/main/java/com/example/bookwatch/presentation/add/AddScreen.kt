package com.example.bookwatch.presentation.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    viewModel: AddViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSearch: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddEffect.NavigateBack -> onNavigateBack()
                is AddEffect.ShowError -> { }
                is AddEffect.NavigateToSearch -> onNavigateToSearch(effect.query)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить книгу") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(AddIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Поле поиска
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.handleIntent(AddIntent.UpdateSearchQuery(it)) },
                label = { Text("Название книги *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.searchQuery.isBlank(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (state.searchQuery.isNotBlank()) {
                                viewModel.handleIntent(AddIntent.NavigateToSearch)
                            }
                        },
                        enabled = state.searchQuery.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Превью выбранной книги
            if (state.selectedBook != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = state.selectedBook!!.thumbnailUrl.ifEmpty {
                                "https://via.placeholder.com/200x300?text=No+Cover"
                            },
                            contentDescription = state.selectedBook!!.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = state.selectedBook!!.title,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = state.selectedBook!!.authors,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = state.selectedBook!!.publishedDate,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        state.selectedBook!!.categories?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.handleIntent(AddIntent.AddSelectedBook) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Добавить в список")
                        }
                    }
                }
            } else {
                // Инструкция когда книга не выбрана
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Поиск книги",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Введите название книги и нажмите на иконку поиска",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}