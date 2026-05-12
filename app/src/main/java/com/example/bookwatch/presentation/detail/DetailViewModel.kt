package com.example.bookwatch.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookwatch.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _effect = Channel<DetailEffect>()
    val effect: Flow<DetailEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.NavigateBack -> navigateBack()
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