package com.arctouch.codechallenge.util

sealed class UiState<T>

class Success<T>(val data: T) : UiState<T>()
class Error<T>(val message: String?) : UiState<T>()
class Loading<T> : UiState<T>()