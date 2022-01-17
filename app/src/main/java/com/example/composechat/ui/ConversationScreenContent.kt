package com.example.composechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.conversation.*
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun ConversationBody(ui: ConversationUiState, modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .navigationBarsWithImePadding()
                .fillMaxSize()
        ) {
            MessageList(ui.messages, scrollState = scrollState, modifier = Modifier.weight(1f))
            UserInputContent({
                ui.addMessage(it)
            })
        }
    }
}


@Composable
fun MessageList(
    messages: List<Message>,
    scrollState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(state = scrollState, reverseLayout = true) {
            for (index in messages.indices) {
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val message = messages[index]
                val isLastMessageByAuthor = nextAuthor != message.author
                val isCurrentAuthor = message.author == "me"
                item {
                    MessageAndAuthor(
                        message = message,
                        isLastMessageByAuthor = isLastMessageByAuthor,
                        isAuthorMe = isCurrentAuthor
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ConversationBodyPreview() {
    val conUi = ConversationUiState("test", 4, initialMessages = initialMessages)
    ConversationBody(ui = conUi)
}

@Composable
fun MessageAndAuthor(
    modifier: Modifier = Modifier,
    message: Message,
    isLastMessageByAuthor: Boolean,
    isAuthorMe: Boolean
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
            isAuthorMe = isAuthorMe
        )
        MessageSection(
            message = message,
            isLastMessageByAuthor = isLastMessageByAuthor,
            isAuthorMe = isAuthorMe
        )
    }
}

@Composable
fun AuthorSection(message: Message, isLastMessageByAuthor: Boolean, isAuthorMe: Boolean) {
    if (isLastMessageByAuthor) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isAuthorMe) {
                Text(text = message.author, style = MaterialTheme.typography.titleMedium)
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
fun MessageSection(message: Message, isLastMessageByAuthor: Boolean, isAuthorMe: Boolean) {
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



