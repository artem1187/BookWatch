package com.example.bookwatch.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookwatch.domain.model.Book
import com.example.bookwatch.domain.usecase.GetBookDescriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getBookDescriptionUseCase: GetBookDescriptionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _effect = Channel<DetailEffect>()
    val effect: Flow<DetailEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadDescription -> loadDescription(intent.openLibraryKey)
            is DetailIntent.NavigateBack -> navigateBack()
        }
    }

    private fun loadDescription(openLibraryKey: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val description = getBookDescriptionUseCase(openLibraryKey)
                _state.update {
                    it.copy(
                        bookDescription = description,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
                _effect.send(DetailEffect.ShowError(e.message ?: "Ошибка загрузки описания"))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(DetailEffect.NavigateBack)
        }
    }

    fun setBook(book: Book) {
        _state.update { it.copy(book = book) }
    }
}