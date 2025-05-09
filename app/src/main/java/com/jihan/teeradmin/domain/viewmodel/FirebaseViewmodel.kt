package com.jihan.teeradmin.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.jihan.teeradmin.data.models.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class FirebaseViewmodel: ViewModel() {

    val db = FirebaseFirestore.getInstance()
    private val _appInfo = MutableStateFlow<AppInfo>(AppInfo())
    val appInfo = _appInfo.asStateFlow()

    init {
        observeAppInfo()
    }


    fun observeAppInfo() {
        db.collection("appInfo").document("appInfo").addSnapshotListener {snapshot,exception ->

            if (snapshot==null || snapshot.exists().not()){
                return@addSnapshotListener
            }

            val appInfo = snapshot.toObject(AppInfo::class.java)
            if (appInfo != null) {
                _appInfo.value = appInfo
            } else {
                _appInfo.value = AppInfo()
            }

        }

    }



    fun updateAppInfo(appInfo: AppInfo,
                      onSuccess: () -> Unit = {},
                        onFailure: (String?) -> Unit = {})
                       {


        db.collection("appInfo").document("appInfo").set(appInfo)
            .addOnSuccessListener {
               onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message)
            }

    }



}