package com.example.composechat

import com.example.composechat.conversation.*
import kotlinx.coroutines.delay

class ChatRepository {
    private val profiles = listOf(meProfile, colleagueProfile)

    suspend fun getConversation(): ConversationUiState.ConversationState {
        delay(2000)
        return ConversationUiState.ConversationState("test", 4, initialMessages = initialMessages)
    }

    suspend fun getProfile(name: String): ProfileUiState {
        delay(3000)
        profiles.forEach {
            if (it.name == name) {
                return it
            }
        }
        throw NotImplementedError()
    }
}