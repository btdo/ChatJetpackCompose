package com.example.composechat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.initialMessages
import com.example.composechat.profile.meProfile
import kotlinx.coroutines.launch

class MainViewModel(val repository: ChatRepository = ChatRepository()) : ViewModel() {
    var conversationState: ConversationUiState =
        ConversationUiState.ConversationState("test", 4, initialMessages = initialMessages)
        private set

    fun getConversationState() {
        viewModelScope.launch {
            conversationState = repository.getConversation()
        }
    }

    val profileUiState = mutableStateOf(meProfile)

    fun getProfile(name: String) {
        viewModelScope.launch {
            profileUiState.value = repository.getProfile(name)
        }
    }

}