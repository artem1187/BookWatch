package com.example.bookwatch.presentation.add

sealed class AddEffect {
    data object NavigateBack : AddEffect()
    data class ShowError(val message: String) : AddEffect()
    data class NavigateToSearch(val query: String) : AddEffect()
}