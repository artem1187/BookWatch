package com.example.bookwatch.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.model.Book
import com.example.bookwatch.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddState())
    val state: StateFlow<AddState> = _state.asStateFlow()

    private val _effect = Channel<AddEffect>()
    val effect: Flow<AddEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is AddIntent.SelectBook -> selectBook(intent.book)
            is AddIntent.AddSelectedBook -> addSelectedBook()
            is AddIntent.ClearSelection -> clearSelection()
            is AddIntent.NavigateBack -> navigateBack()
            is AddIntent.NavigateToSearch -> navigateToSearch()
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun selectBook(book: SearchBook) {
        _state.update { it.copy(selectedBook = book) }
    }

    private fun addSelectedBook() {
        viewModelScope.launch {
            val book = _state.value.selectedBook ?: return@launch

            try {
                val domainBook = Book(
                    title = book.title,
                    authors = book.authors,
                    publishedDate = book.publishedDate,
                    thumbnailUrl = book.thumbnailUrl,
                    categories = book.categories,
                    description = book.description,
                    pageCount = book.pageCount,
                    openLibraryKey = book.key
                )
                addBookUseCase(domainBook)
                _effect.send(AddEffect.NavigateBack)
            } catch (e: Exception) {
                _effect.send(AddEffect.ShowError(e.message ?: "Ошибка добавления"))
            }
        }
    }

    private fun clearSelection() {
        _state.update { it.copy(selectedBook = null) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(AddEffect.NavigateBack)
        }
    }

    private fun navigateToSearch() {
        val query = _state.value.searchQuery
        if (query.isNotBlank()) {
            viewModelScope.launch {
                _effect.send(AddEffect.NavigateToSearch(query))
            }
        }
    }
}