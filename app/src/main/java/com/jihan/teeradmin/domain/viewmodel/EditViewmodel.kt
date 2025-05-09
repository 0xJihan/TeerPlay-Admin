package com.jihan.teeradmin.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel for Edit Match Screen
class EditMatchViewModel(
) : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isInitialLoading = MutableStateFlow(false)
    val isInitialLoading = _isInitialLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val db = FirebaseFirestore.getInstance()


    fun updateMatch(matchId: String, matchDetail: Map<String, Any?>) {
        viewModelScope.launch {
            _isLoading.value = true

            try {

                db.collection("matches").document(matchId).update(matchDetail).await()

                _isSuccess.value = true
                delay(1000)  // Show success for 2 seconds
                _isSuccess.value = false

            } catch (e: Exception) {
                _errorMessage.value = e.message
                delay(3000)
                _errorMessage.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun deleteMatch(
        matchId: String, onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                db.collection("matches").document(matchId).delete().await()
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete: ${e.message}"
                _isLoading.value = false
            }
        }
    }
}