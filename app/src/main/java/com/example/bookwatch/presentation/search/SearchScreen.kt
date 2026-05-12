package com.example.bookwatch.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.presentation.components.SearchResultItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    query: String,
    onNavigateBack: () -> Unit,
    onBookSelected: (SearchBook) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(query) {
        viewModel.handleIntent(SearchIntent.Search(query))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.BookSelected -> onBookSelected(effect.book)
                is SearchEffect.NavigateBack -> onNavigateBack()
                is SearchEffect.ShowError -> { }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результаты поиска") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(SearchIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = onNavigateBack) {
                            Text("Назад")
                        }
                    }
                }

                state.searchResults.isEmpty() -> {
                    Text(
                        text = "Ничего не найдено",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.searchResults) { book ->
                            SearchResultItem(
                                book = book,
                                onClick = {
                                    viewModel.handleIntent(SearchIntent.SelectBook(book))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}