package com.aipromptgenerater.aitricker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipromptgenerater.aitricker.data.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    private val _credits = MutableStateFlow(0)
    val credits = _credits.asStateFlow()

    private val _generatedPrompt = MutableStateFlow<String?>(null)
    val generatedPrompt = _generatedPrompt.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun fetchCredits() {
        viewModelScope.launch {
            _credits.value = repository.getWalletCredits()
        }
    }

    fun generate(type: String, name: String, idea: String, tech: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = repository.generatePrompt(type, name, idea, tech)
            result.onSuccess {
                _generatedPrompt.value = it
                fetchCredits() // Refresh credits after deduction
            }.onFailure {
                _error.value = it.message
            }
            
            _isLoading.value = false
        }
    }
}
