package com.ithersta.anketa.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val displayName: String,
    val email: String,
)