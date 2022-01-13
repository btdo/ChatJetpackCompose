package com.example.composechat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.conversation.*

@Composable
fun ConversationBody(modifier: Modifier = Modifier, ui: ConversationUiState) {
    val scrollState = rememberLazyListState()
    Surface(modifier = modifier) {
        Messages(ui.messages, scrollState = scrollState)
    }
}

@Composable
fun MessageInput() {

}

@Composable
fun Messages(messages: List<Message>, scrollState: LazyListState = rememberLazyListState()) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = scrollState, reverseLayout = true) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val message = messages[index]
                val isFirstMessageByAuthor = prevAuthor != message.author
                val isLastMessageByAuthor = nextAuthor != message.author
                val isCurrentAuthor = message.author == "me"
                item {
                    Message(
                        message = message,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
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
fun MessagesPreview() {
    val conUi = ConversationUiState("test", 4, initialMessages = initialMessages)
    ConversationBody(ui = conUi)
}

@Composable
fun Message(
    modifier: Modifier = Modifier,
    message: Message,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    isAuthorMe: Boolean
) {
    val padding = if (isAuthorMe) Modifier.padding(start= 24.dp, end = 8.dp) else Modifier.padding(start = 8.dp, end = 24.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(padding),
        horizontalAlignment = if (isAuthorMe) Alignment.End else Alignment.Start
    ) {
        if (isLastMessageByAuthor) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = message.author)
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        val backgroundBubbleColor = if (isAuthorMe) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }

        val chatBubble = if (isAuthorMe) ChatBubbleShapeMe else ChatBubbleShape

        Surface(color = backgroundBubbleColor, shape = chatBubble) {
            ClickableText(message = message, isAuthorMe = isAuthorMe, {})
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



