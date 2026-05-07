package com.example.bookwatch.presentation.main

sealed class MainIntent {
    data object LoadBooks : MainIntent()
    data object DeleteSelected : MainIntent()
    data class ToggleSelection(val bookId: Int) : MainIntent()
    data object NavigateToAdd : MainIntent()
    data class NavigateToDetail(val bookId: Int) : MainIntent()
    data object ShowDeleteDialog : MainIntent()
    data object HideDeleteDialog : MainIntent()
}