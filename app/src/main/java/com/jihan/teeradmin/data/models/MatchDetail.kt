package com.jihan.teeradmin.data.models

data class MatchDetail(
    val id: Int,
    val title: String,
    val date: String,
    val frNumber: Int?=null,
    val srNumber: Int?=null,
    val frTime: String,
    val srTime: String,
    val isFrOpened: Boolean,
    val isSrOpened: Boolean,
    val documentId: String
)