package com.example.composechat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.profile.ProfileUiState
import kotlinx.coroutines.launch

class MainViewModel(val repository: ChatRepository = ChatRepository()) : ViewModel() {
    var conversationState = mutableStateOf<ConversationUiState>(ConversationUiState.Loading)

    fun getConversationState() {
        viewModelScope.launch {
            conversationState.value = repository.getConversation()
        }
    }

    var profileUiState = mutableStateOf<ProfileUiState>(ProfileUiState.Loading)

    fun getProfile(name: String) {
        viewModelScope.launch {
            profileUiState.value = ProfileUiState.Loading
            profileUiState.value = repository.getProfile(name)
        }
    }

}