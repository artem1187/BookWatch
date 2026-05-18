package com.example.bookwatch.presentation.detail

sealed class DetailIntent {
    data class LoadDescription(val openLibraryKey: String) : DetailIntent()
    data object NavigateBack : DetailIntent()
}