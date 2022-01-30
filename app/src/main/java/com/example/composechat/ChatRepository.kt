package com.example.composechat

import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.Message
import com.example.composechat.conversation.initialMessages
import com.example.composechat.profile.ProfileUiState
import com.example.composechat.profile.colleagueProfile
import com.example.composechat.profile.colleagueProfile2
import com.example.composechat.profile.meProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class ChatRepository {
    private val profiles = listOf(meProfile, colleagueProfile, colleagueProfile2)
    private val _conversation =
        MutableStateFlow(ConversationUiState.ConversationState("", 0, initialMessages = listOf()))

    suspend fun getConversation(): ConversationUiState.ConversationState =
        withContext(Dispatchers.IO) {
            delay(2000)
            _conversation.value =
                ConversationUiState.ConversationState("test", 4, initialMessages = initialMessages)
            return@withContext _conversation.value
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

    suspend fun addMessage(message: Message) {
        delay(500)
        val currentCon = _conversation.value
        currentCon.addMessage(message)
        _conversation.value = currentCon
    }
}