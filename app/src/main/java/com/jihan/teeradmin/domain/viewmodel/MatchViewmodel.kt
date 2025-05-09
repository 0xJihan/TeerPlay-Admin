package com.jihan.teeradmin.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.jihan.teeradmin.data.models.MatchDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.mapNotNull

class MatchViewmodel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Add loading state to handle UI states better
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // Add error handling
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _matches = MutableStateFlow<List<MatchDetail>>(emptyList())
    val matches = _matches.asStateFlow()

    init {
        observeMatches()
    }

    fun observeMatches() {
        _isLoading.value = true
        db.collection("matches")
            .addSnapshotListener { snapshot, e ->
                _isLoading.value = false

                if (e != null) {
                    Log.e("MatchViewmodel", "Error getting matches", e)
                    _error.value = "Failed to load matches: ${e.localizedMessage}"
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    _matches.value = emptyList()
                    return@addSnapshotListener
                }

                try {
                    val matchList = snapshot.documents.mapNotNull { item ->
                        try {
                            MatchDetail(
                                id = item.getLong("id")?.toInt() ?: item.id.hashCode(),
                                title = item.getString("title") ?: "N/A",
                                date = item.getString("date") ?: "N/A",
                                frNumber = item.getLong("frNumber")?.toInt(),
                                srNumber = item.getLong("srNumber")?.toInt(),
                                frTime = item.getString("frTime") ?: "N/A",
                                srTime = item.getString("srTime") ?: "N/A",
                                isFrOpened = item.getBoolean("isFrOpened") == true,
                                isSrOpened = item.getBoolean("isSrOpened") == true,
                                documentId = item.id
                            )
                        } catch (e: Exception) {
                            Log.e("MatchViewmodel", "Error parsing match: ${e.localizedMessage}")
                            null
                        }
                    }

                    _matches.value = matchList
                } catch (e: Exception) {
                    Log.e("MatchViewmodel", "Error processing matches data", e)
                    _error.value = "Failed to process matches: ${e.localizedMessage}"
                }
            }
    }


    fun getMatchDetailById(matchId: String): MatchDetail? {
        return _matches.value.find { it.documentId == matchId }
    }


    fun deleteMatch(matchId: String,onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        _isLoading.value = true
        db.collection("matches")
            .document(matchId)
            .delete()
            .addOnSuccessListener {
                _isLoading.value = false
               onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("MatchViewmodel", "Error deleting match", e)
                _error.value = "Failed to delete match: ${e.localizedMessage}"
                onFailure(e.localizedMessage ?: "Unknown error")
                _isLoading.value = false
            }
    }

    // Add a function to retry fetching if there was an error
    fun retryFetchMatches() {
        _error.value = null
        observeMatches()
    }
}