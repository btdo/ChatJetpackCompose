package com.example.composechat.profile

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.example.composechat.R

sealed class ProfileUiState {
    object Loading : ProfileUiState()

    @Immutable
    data class ProfileState(
        val userId: String,
        @DrawableRes val photo: Int?,
        val name: String,
        val status: String,
        val displayName: String,
        val position: String,
        val twitter: String = "",
        val timeZone: String?, // Null if me
        val commonChannels: String? // Null if me
    ) : ProfileUiState() {
        fun isMe() = userId == meProfile.userId
    }
}

/**
 * Example colleague profile
 */
val colleagueProfile = ProfileUiState.ProfileState(
    userId = "12345",
    photo = R.drawable.someone_else,
    name = "Taylor Brooks",
    status = "Away",
    displayName = "taylor",
    position = "Senior Android Dev at Openlane",
    twitter = "twitter.com/taylorbrookscodes",
    timeZone = "12:25 AM local time (Eastern Daylight Time)",
    commonChannels = "2"
)

/**
 * Example colleague profile
 */
val colleagueProfile2 = ProfileUiState.ProfileState(
    userId = "41235",
    photo = R.drawable.someone_else,
    name = "John Glenn",
    status = "Away",
    displayName = "jglenn",
    position = "Senior Android Dev at Openlane",
    twitter = "twitter.com/taylorbrookscodes",
    timeZone = "12:25 AM local time (Eastern Daylight Time)",
    commonChannels = "2"
)

/**
 * Example "me" profile.
 */
val meProfile = ProfileUiState.ProfileState(
    userId = "me",
    photo = R.drawable.ali,
    name = "Ali Conors",
    status = "Online",
    displayName = "aliconors",
    position = "Senior Android Dev at Yearin\nGoogle Developer Expert",
    twitter = "twitter.com/aliconors",
    timeZone = "In your timezone",
    commonChannels = null
)