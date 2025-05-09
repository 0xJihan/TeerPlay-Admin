package com.jihan.teeradmin.data.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.jihan.teeradmin.domain.viewmodel.EditMatchViewModel
import com.jihan.teeradmin.domain.viewmodel.FirebaseViewmodel
import com.jihan.teeradmin.domain.viewmodel.MatchViewmodel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.kotlinx.serialization.asConverterFactory


private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
}

val appModule = module {
    val jsonConverterFactory = json.asConverterFactory("application/json; charset=UTF-8".toMediaType())

    single {
        MatchViewmodel()
    }

    single {
        FirebaseViewmodel()
    }

    viewModel {
        EditMatchViewModel()
    }
}


