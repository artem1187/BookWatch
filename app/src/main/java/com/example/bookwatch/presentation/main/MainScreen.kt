package com.example.bookwatch.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookwatch.presentation.components.BookListItem
import com.example.bookwatch.presentation.components.DeleteConfirmationDialog
import com.example.bookwatch.presentation.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainEffect.NavigateToAdd -> onNavigateToAdd()
                is MainEffect.NavigateToDetail -> onNavigateToDetail(effect.bookId)
                is MainEffect.ShowMessage -> { }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои книги") },
                actions = {
                    if (state.books.any { it.isSelected }) {
                        IconButton(onClick = { viewModel.handleIntent(MainIntent.ShowDeleteDialog) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Удалить выбранные"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleIntent(MainIntent.NavigateToAdd) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить книгу"
                )
            }
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
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.handleIntent(MainIntent.LoadBooks) }) {
                            Text("Повторить")
                        }
                    }
                }

                state.books.isEmpty() -> {
                    EmptyState(
                        message = "Нет выбранных книг\nНажмите + чтобы добавить",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.books) { book ->
                            BookListItem(
                                book = book,
                                onCheckChanged = {
                                    viewModel.handleIntent(MainIntent.ToggleSelection(book.id))
                                },
                                onItemClick = {
                                    viewModel.handleIntent(MainIntent.NavigateToDetail(book.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = { viewModel.handleIntent(MainIntent.DeleteSelected) },
            onDismiss = { viewModel.handleIntent(MainIntent.HideDeleteDialog) }
        )
    }
}