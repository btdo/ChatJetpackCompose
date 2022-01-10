package com.example.composechat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.composechat.ui.ConversationBody
import com.example.composechat.ui.ProfileBody
import com.example.composechat.ui.TopBar

enum class ChatScreen(val icon: ImageVector, val title: String, val route: String, val body: @Composable () -> Unit) {
    Conversation (Icons.Filled.AccountBox, "Conversation", "conversation", {
        ConversationBody()
    }),
    Profile(Icons.Filled.Person, "Profile", "profile", {
        ProfileBody()
    });

    companion object {
        fun fromRoute(route: String?): ChatScreen =
            when (route?.substringBefore("/")) {
                Conversation.route -> Conversation
                Profile.route -> Profile
                null -> Conversation
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

val chatScreens =  listOf(ChatScreen.Conversation, ChatScreen.Profile)

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