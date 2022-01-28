package com.example.composechat

import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.initialMessages
import com.example.composechat.profile.ProfileUiState
import com.example.composechat.profile.colleagueProfile
import com.example.composechat.profile.colleagueProfile2
import com.example.composechat.profile.meProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ChatRepository {
    private val profiles = listOf(meProfile, colleagueProfile, colleagueProfile2)

    suspend fun getConversation(): ConversationUiState.ConversationState {
        delay(2000)
        return ConversationUiState.ConversationState("test", 4, initialMessages = initialMessages)
    }

    suspend fun getProfile(name: String): ProfileUiState.ProfileState =
        withContext(Dispatchers.IO) {
            delay(2000)
            profiles.forEach {
                if (it.displayName == name) {
                    return@withContext it
                }
            }
            throw NotImplementedError()
        }
}