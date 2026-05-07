package com.example.bookwatch.presentation.main

sealed class MainEffect {
    data object NavigateToAdd : MainEffect()
    data class NavigateToDetail(val bookId: Int) : MainEffect()
    data class ShowMessage(val message: String) : MainEffect()
}