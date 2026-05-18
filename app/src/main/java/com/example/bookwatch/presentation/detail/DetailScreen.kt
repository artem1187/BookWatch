package com.example.bookwatch.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.bookwatch.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    book: Book,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Загружаем описание при первом показе
    LaunchedEffect(book) {
        viewModel.setBook(book)
        book.openLibraryKey?.let { key ->
            viewModel.handleIntent(DetailIntent.LoadDescription(key))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailEffect.NavigateBack -> onNavigateBack()
                is DetailEffect.ShowError -> { }
            }
        }
    }

    val currentBook = state.book ?: book
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentBook.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(DetailIntent.NavigateBack) }) {
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
                .verticalScroll(scrollState)
        ) {
            // Обложка
            AsyncImage(
                model = currentBook.thumbnailUrl.ifEmpty {
                    "https://via.placeholder.com/300x450?text=No+Cover"
                },
                contentDescription = currentBook.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            // Информация о книге
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = currentBook.title,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = currentBook.authors,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentBook.publishedDate,
                    style = MaterialTheme.typography.bodyLarge
                )

                currentBook.categories?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // ОПИСАНИЕ КНИГИ
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.bookDescription != null) {
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.bookDescription!!,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if (state.error != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Не удалось загрузить описание",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                } else {
                    Text(
                        text = "Описание отсутствует",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                currentBook.pageCount?.let { pages ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Страниц: $pages",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}