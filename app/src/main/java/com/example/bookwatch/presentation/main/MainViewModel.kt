package com.example.bookwatch.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookwatch.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val deleteSelectedBooksUseCase: DeleteSelectedBooksUseCase,
    private val toggleBookSelectionUseCase: ToggleBookSelectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = Channel<MainEffect>()
    val effect: Flow<MainEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(MainIntent.LoadBooks)
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadBooks -> loadBooks()
            is MainIntent.DeleteSelected -> deleteSelected()
            is MainIntent.ToggleSelection -> toggleSelection(intent.bookId)
            is MainIntent.NavigateToAdd -> navigateToAdd()
            is MainIntent.NavigateToDetail -> navigateToDetail(intent.bookId)
            is MainIntent.ShowDeleteDialog -> showDeleteDialog()
            is MainIntent.HideDeleteDialog -> hideDeleteDialog()
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getBooksUseCase()
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
                .collect { books ->
                    _state.update { it.copy(books = books, isLoading = false) }
                }
        }
    }

    private fun deleteSelected() {
        viewModelScope.launch {
            try {
                deleteSelectedBooksUseCase()
                _state.update { it.copy(showDeleteDialog = false) }
                _effect.send(MainEffect.ShowMessage("Книги удалены"))
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun toggleSelection(bookId: Int) {
        viewModelScope.launch {
            val book = _state.value.books.find { it.id == bookId } ?: return@launch
            toggleBookSelectionUseCase(book)
        }
    }

    private fun navigateToAdd() {
        viewModelScope.launch {
            _effect.send(MainEffect.NavigateToAdd)
        }
    }

    private fun navigateToDetail(bookId: Int) {
        viewModelScope.launch {
            _effect.send(MainEffect.NavigateToDetail(bookId))
        }
    }

    private fun showDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = true) }
    }

    private fun hideDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = false) }
    }
}