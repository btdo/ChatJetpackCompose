package com.example.composechat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.composechat.ui.TopBar

sealed class ChatScreen(open val icon: ImageVector, open val title: String, open val route: String, open val body: @Composable () -> Unit) {
    data class ConversationScreen(
        override val icon: ImageVector,
        override val title: String,
        override val body: @Composable () -> Unit
    ) : ChatScreen(icon, title, "conversation", body)

    data class ProfileScreen(
        override val icon: ImageVector,
        override val title: String,
        override val body: @Composable () -> Unit
    ) : ChatScreen(icon, title, "profile", body)
}

@Composable
fun ChatScreenScaffold(
    title: String,
    body: @Composable () -> Unit,
    onDrawerClicked: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopBar(title = title) {
            onDrawerClicked()
        }
        body()
    }
}