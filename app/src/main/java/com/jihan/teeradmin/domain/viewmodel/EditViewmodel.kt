package com.jihan.teeradmin.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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

    fun moveToHistory(
        matchId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val matchDocRef = db.collection("matches").document(matchId)
                val snapshot = matchDocRef.get().await()

                if (!snapshot.exists()) {
                    onFailure("Match not found.")
                    return@launch
                }

                val matchData = snapshot.data
                if (matchData == null) {
                    onFailure("Match data is null.")
                    return@launch
                }

                // Optionally add or update fields before moving
                matchData["isHistory"] = true

                val historyDocRef = db.collection("history").document(matchId)
                historyDocRef.set(matchData).await()

                matchDocRef.delete().await()

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.localizedMessage ?: "Unknown error")
            }
        }
    }




    @Suppress("UNCHECKED_CAST")
    fun distributePrizes(
        winNumber: String,
        numberMultiplier: Double,
        houseAndEndingMultiplier: Double,
        matchId: String,
        isFr: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val matchBetsRef = firestore.collection("match-bets").document(matchId)

                // Get match-bets document
                val matchBetsDoc = matchBetsRef.get().await()
                if (!matchBetsDoc.exists()) {
                    Log.e("PrizeDistribution", "Match bets document not found: $matchId")
                    onError("Match bets document not found")
                    return@launch
                }

                // Get the round prefix (fr or sr)
                val roundPrefix = if (isFr) "fr" else "sr"

                // Get the bet data for each category

                val numberBets = matchBetsDoc.get("${roundPrefix}_number") as? Map<String, Map<String, Any>> ?: emptyMap()
                val houseBets = matchBetsDoc.get("${roundPrefix}_house") as? Map<String, Map<String, Any>> ?: emptyMap()
                val endingBets = matchBetsDoc.get("${roundPrefix}_ending") as? Map<String, Map<String, Any>> ?: emptyMap()

                // Extract the house and ending digits from the winning number
                val houseDigit = if (winNumber.isNotEmpty()) winNumber.first().toString() else ""
                val endingDigit = if (winNumber.isNotEmpty()) winNumber.last().toString() else ""

                // Track all users who won and their winnings
                val userWinnings = mutableMapOf<String, Double>()

                // Process exact number bets
                processWinnings(
                    bets = numberBets[winNumber] ?: emptyMap(),
                    multiplier = numberMultiplier,
                    userWinnings = userWinnings
                )

                // Process house (first digit) bets
                processWinnings(
                    bets = houseBets[houseDigit] ?: emptyMap(),
                    multiplier = houseAndEndingMultiplier,
                    userWinnings = userWinnings
                )

                // Process ending (last digit) bets
                processWinnings(
                    bets = endingBets[endingDigit] ?: emptyMap(),
                    multiplier = houseAndEndingMultiplier,
                    userWinnings = userWinnings
                )

                // Update all user balances in a single batch
                val batch = firestore.batch()

                // Process each user's winnings and update their balance
                userWinnings.forEach { (userId, winAmount) ->
                    val userRef = firestore.collection("users").document(userId)
                    batch.update(userRef, "balance", FieldValue.increment(winAmount))


                    // Log the win for debugging
                    Log.d("PrizeDistribution", "User $userId won $winAmount for match $matchId")
                }


                // Commit all changes
                batch.commit().await()

                Log.d("PrizeDistribution", "Successfully distributed prizes for match $matchId")

                onSuccess()
            } catch (e: Exception) {

                onError("Error distributing prizes: ${e.localizedMessage}")
                Log.e("PrizeDistribution", "Error distributing prizes", e)

            }
        }
    }


    private fun processWinnings(
        bets: Map<String, Any>,
        multiplier: Double,
        userWinnings: MutableMap<String, Double>
    ) {
        bets.forEach { (userId, betAmount) ->
            val amount = when (betAmount) {
                is Long -> betAmount.toDouble()
                is Double -> betAmount
                else -> 0.0
            }

            val winAmount = amount * multiplier

            // Add to user's total winnings (accumulate if they've won from multiple bet types)
            userWinnings[userId] = (userWinnings[userId] ?: 0.0) + winAmount
        }
    }

}