package com.example.composechat.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.R
import com.example.composechat.conversation.*
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.launch


@Composable
@Preview
fun ConversationBodyPreview() {
    val conUi = ConversationUiState.ConversationState("test", 4, initialMessages = initialMessages)
    ConversationBody(conUiState = conUi, {}, {})
}

@Composable
fun ConversationBody(
    conUiState: ConversationUiState,
    onProfileClicked: (String) -> Unit,
    onMessageAdd: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("ConversationBody", "recomposing $conUiState")

    if (conUiState is ConversationUiState.Loading) {
        LoadingScreen()
        return
    }

    val ui = (conUiState as ConversationUiState.ConversationState)
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isScrolling by remember {
        derivedStateOf {
            scrollState.isScrollInProgress
        }
    }

    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .navigationBarsWithImePadding()
                .fillMaxSize()
        ) {
            MessageList(
                messages = ui.messages,
                onProfileClicked = onProfileClicked,
                scrollState = scrollState,
                resetScroll = {
                    scope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier.weight(1f)
            )
            UserInput(isScrolling = isScrolling, onMessageSend = {
                onMessageAdd(it)
            })
        }
    }
}


@Composable
fun MessageList(
    messages: List<Message>,
    onProfileClicked: (String) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
    resetScroll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val jumpToBottomEnabled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex != 0
        }
    }

    Box(modifier = modifier) {
        LazyColumn(state = scrollState, reverseLayout = true) {
            for (index in messages.indices) {
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val message = messages[index]
                val isLastMessageByAuthor = nextAuthor != message.author
                val isCurrentAuthor = message.author == "aliconors"
                item {
                    MessageAndAuthor(
                        message = message,
                        isLastMessageByAuthor = isLastMessageByAuthor,
                        isAuthorMe = isCurrentAuthor,
                        onProfileClicked = onProfileClicked
                    )
                }
            }
        }

        JumpToBottom(jumpToBottomEnabled, {
            resetScroll()
        }, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun JumpToBottom(isEnabled: Boolean, onClicked: () -> Unit, modifier: Modifier = Modifier) {
    if (isEnabled) {
        FloatingActionButton(modifier = modifier, onClick = onClicked) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowDownward,
                    modifier = Modifier.height(18.dp),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(id = R.string.jumpBottom),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
@Preview
fun JumpToBottomPreview() {
    JumpToBottom(isEnabled = true, onClicked = { })
}


@Composable
fun MessageAndAuthor(
    message: Message,
    isLastMessageByAuthor: Boolean,
    isAuthorMe: Boolean,
    onProfileClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val padding = if (isAuthorMe) Modifier.padding(
        start = 24.dp,
        end = 8.dp
    ) else Modifier.padding(start = 8.dp, end = 24.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(padding),
        horizontalAlignment = if (isAuthorMe) Alignment.End else Alignment.Start
    ) {
        AuthorSection(
            message = message,
            isLastMessageByAuthor = isLastMessageByAuthor,
            isAuthorMe = isAuthorMe,
            onProfileClicked = onProfileClicked
        )
        MessageSection(
            message = message,
            isAuthorMe = isAuthorMe
        )
    }
}

@Composable
fun AuthorSection(
    message: Message,
    isLastMessageByAuthor: Boolean,
    isAuthorMe: Boolean,
    onProfileClicked: (String) -> Unit,
) {
    if (isLastMessageByAuthor) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            onProfileClicked(message.author)
        }) {
            if (isAuthorMe) {
                Text(text = "me", style = MaterialTheme.typography.titleMedium)
            } else {
                Image(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(42.dp)
                        .border(1.5.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .clip(CircleShape),
                    painter = painterResource(id = message.authorImage),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Author"
                )
                Text(text = message.author, style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    } else {
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun MessageSection(message: Message, isAuthorMe: Boolean) {
    val backgroundBubbleColor = if (isAuthorMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val chatBubble = if (isAuthorMe) ChatBubbleShapeMe else ChatBubbleShape

    Surface(color = backgroundBubbleColor, shape = chatBubble) {
        Column(Modifier.padding(12.dp)) {
            ClickableText(message = message, isAuthorMe = isAuthorMe, {})
            message.image?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(color = backgroundBubbleColor, shape = chatBubble) {
                    Image(
                        painter = painterResource(id = it),
                        contentScale = ContentScale.Fit, contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun ClickableText(message: Message, isAuthorMe: Boolean, authorClicked: (String) -> Unit) {
    val uriHandler = LocalUriHandler.current
    val styledMessage = messageFormatter(
        text = message.content,
        primary = isAuthorMe
    )

    androidx.compose.foundation.text.ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(8.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val ChatBubbleShapeMe = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)



