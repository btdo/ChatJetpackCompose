package com.example.composechat.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composechat.conversation.ConversationUiState

@Composable
fun ConversationBody(modifier: Modifier = Modifier, ui: ConversationUiState){
    Text(text = "Conversation Body")
}