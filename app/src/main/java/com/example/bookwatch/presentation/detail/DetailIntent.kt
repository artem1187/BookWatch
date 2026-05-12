package com.example.bookwatch.presentation.detail

sealed class DetailIntent {
    data object NavigateBack : DetailIntent()
}