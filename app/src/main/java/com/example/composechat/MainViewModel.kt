package com.example.composechat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.profile.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(val repository: ChatRepository = ChatRepository()) : ViewModel() {
    private val _conversationState =
        MutableStateFlow<ConversationUiState>(ConversationUiState.Loading)
    val conversationState: StateFlow<ConversationUiState> = _conversationState

    fun getConversationState() {
        viewModelScope.launch {
            _conversationState.value = repository.getConversation()
        }
    }

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    fun getProfile(name: String) {
        _profileUiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            _profileUiState.value = repository.getProfile(name)
        }
    }

}