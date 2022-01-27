package com.example.composechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.ChatScreen
import com.example.composechat.R
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.initialMessages
import com.example.composechat.profile.meProfile
import com.example.composechat.ui.theme.ComposeChatTheme

@Composable
fun TopBar(title: String, onDrawerClicked: () -> Unit) {
    TopAppBar(title = {
        Text(text = title)
    }, navigationIcon = {
        IconButton(onClick = { onDrawerClicked() }) {
            Icon(Icons.Filled.Menu, contentDescription = "")
        }
    } )
}

@Composable
fun ChatDrawer(
    modifier: Modifier = Modifier,
    screens: List<ChatScreen>,
    selected: ChatScreen,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 12.dp)) {
        DrawerHeader()
        Divider(
            modifier.padding(top = 5.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        )
        screens.forEach {
            DrawerItem(
                Modifier.clickable {
                    onDestinationClicked(it.route)
                },
                it, it == selected,
            )
        }
    }

}

@Composable
fun DrawerHeader() {
    Row {
        Box(modifier = Modifier.size(24.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_jetchat_front),
                contentDescription = null,
                tint = MaterialTheme.colors.primarySurface
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_jetchat_back),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
        Image(
            painter = painterResource(id = R.drawable.jetchat_logo),
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
@Preview
fun DrawerHeaderPreview() {
    DrawerHeader()
}

@Composable
fun DrawerItem(modifier: Modifier = Modifier, screen: ChatScreen, selected: Boolean) {
    val background = if (selected) {
        Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }

    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(CircleShape)
            .then(background), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
            Icon(
                painter = painterResource(id = R.drawable.ic_jetchat_front),
                contentDescription = null,
                tint = MaterialTheme.colors.primarySurface
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp)
        ) {
            Text(screen.title)
        }
    }
}

@Composable
@Preview
fun ChatDrawerPreview() {
    val conversation = ChatScreen.ConversationScreen(
        Icons.Filled.AccountBox,
        "Conversation"
    )
    {
        ConversationBody(conUiState = ConversationUiState.ConversationState(
            "test",
            4,
            initialMessages = initialMessages
        ), onProfileClicked = {})
    }
    val profile = ChatScreen.ProfileScreen(Icons.Filled.Person, "Profile") {
        ProfileBody(meProfile)
    }
    val screens = listOf(conversation, profile)

    ComposeChatTheme {
        Surface {
            ChatDrawer( screens = screens, selected = conversation, onDestinationClicked = {})
        }
    }
}
