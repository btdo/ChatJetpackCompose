package com.example.composechat

import androidx.lifecycle.ViewModel
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.ProfileScreenState
import com.example.composechat.conversation.initialMessages
import com.example.composechat.conversation.meProfile

class MainViewModel: ViewModel() {
    var conversationUI: ConversationUiState =
        ConversationUiState("test", 4, initialMessages = initialMessages)
        private set

    var profileScreenState: ProfileScreenState = meProfile
}