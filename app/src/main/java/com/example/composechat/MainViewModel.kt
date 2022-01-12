package com.example.composechat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.Message
import com.example.composechat.conversation.initialMessages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {
    var conversationUI : ConversationUiState = ConversationUiState("test", 4, initialMessages = initialMessages)
       private set
}