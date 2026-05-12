package com.example.bookwatch.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.usecase.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect: Flow<SearchEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Search -> searchBooks(intent.query)
            is SearchIntent.SelectBook -> selectBook(intent.book)
            is SearchIntent.ClearResults -> clearResults()
            is SearchIntent.NavigateBack -> navigateBack()
        }
    }

    private fun searchBooks(query: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    query = query
                )
            }

            try {
                val results = searchBooksUseCase(query)
                _state.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false
                    )
                }

                if (results.isEmpty()) {
                    _state.update { it.copy(error = "Ничего не найдено") }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Ошибка поиска",
                        isLoading = false
                    )
                }
                _effect.send(SearchEffect.ShowError(e.message ?: "Ошибка поиска"))
            }
        }
    }

    private fun selectBook(book: SearchBook) {
        viewModelScope.launch {
            _effect.send(SearchEffect.BookSelected(book))
        }
    }

    private fun clearResults() {
        _state.update { it.copy(searchResults = emptyList(), error = null) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(SearchEffect.NavigateBack)
        }
    }
}