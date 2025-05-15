package com.jihan.teeradmin.domain.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel for Create Match Screen
class CreateMatchViewModel : ViewModel() {
    // UI State
    val title = mutableStateOf("")
    val date = mutableStateOf("")
    val firstRoundNumber = mutableStateOf("")
    val secondRoundNumber = mutableStateOf("")
    val firstRoundTime = mutableStateOf("")
    val secondRoundTime = mutableStateOf("")
    val matchId = mutableStateOf("")
    val numberMultiplier = mutableStateOf("")
    val homeMultiplier = mutableStateOf("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun createMatch() {
        _errorMessage.value= null
        viewModelScope.launch {
            if (!validateInputs()) {
                return@launch
            }

            _isLoading.value = true

            try {
                val matchDetail = hashMapOf(
                    "title" to title.value,
                    "date" to date.value,
                    "frNumber" to firstRoundNumber.value.toIntOrNull(),
                    "srNumber" to secondRoundNumber.value.toIntOrNull(),
                    "frTime" to firstRoundTime.value,
                    "srTime" to secondRoundTime.value,
                    "isFrOpened" to true,
                    "isSrOpened" to true,
                    "createdAt" to Timestamp.now(),
                    "id" to matchId.value.toIntOrNull(),
                    "numberMultiplier" to numberMultiplier.value.toLong(),
                    "homeMultiplier" to homeMultiplier.value.toLong(),
                )

                db.collection("matches")
                    .add(matchDetail)
                    .await()

                _isSuccess.value = true
                delay(2000)  // Show success for 2 seconds
                _isSuccess.value = false
                clearFields()

            } catch (e: Exception) {
                _errorMessage.value = e.message
                delay(3000)
                _errorMessage.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun validateInputs(): Boolean {
        when {
            title.value.isBlank() -> {
                _errorMessage.value = "Please enter a title"
                return false
            }
            date.value.isBlank() -> {
                _errorMessage.value = "Please enter a date"
                return false
            }
            firstRoundTime.value.isBlank() -> {
                _errorMessage.value = "Please enter first runner time"
                return false
            }
            secondRoundTime.value.isBlank() -> {
                _errorMessage.value = "Please enter second runner time"
                return false
            }
            matchId.value.toIntOrNull()==null ->{
                _errorMessage.value = "Please enter a valid match ID"
                return false
            }
            numberMultiplier.value.toLongOrNull()==null-> {
                "Please enter a valid Number Multiplier"
                return false
            }
            homeMultiplier.value.toLongOrNull()==null->{
                "Please enter valid Home & End Multiplier"
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun clearFields() {
        title.value = ""
        date.value = ""
        firstRoundNumber.value = ""
        secondRoundNumber.value = ""
        firstRoundTime.value = ""
        secondRoundTime.value = ""
        matchId.value = ""
        numberMultiplier.value=""
        homeMultiplier.value=""
    }
}