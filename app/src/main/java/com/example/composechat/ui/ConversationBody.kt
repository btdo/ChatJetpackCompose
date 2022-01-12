package com.example.composechat.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.conversation.ConversationUiState
import com.example.composechat.conversation.Message
import com.example.composechat.conversation.initialMessages

@Composable
fun ConversationBody(modifier: Modifier = Modifier, ui: ConversationUiState) {
    val scrollState = rememberLazyListState()
    Surface(modifier = modifier) {
        Messages(ui.messages.reversed(), scrollState = scrollState)
    }
}

@Composable
fun MessageInput() {

}

@Composable
fun Messages(messages: List<Message>, scrollState: LazyListState = rememberLazyListState()) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = scrollState) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val message = messages[index]
                val isFirstMessageByAuthor = prevAuthor == message.author
                val isLastMessageByAuthor = nextAuthor == message.author
                val isCurrentAuthor = message.author == "me"
                val leftOrRightAlign = if (isCurrentAuthor) Modifier.padding(start= 12.dp, end = 48.dp) else Modifier.padding(start = 48.dp, end = 12.dp)
                item {
                    Message(modifier = leftOrRightAlign,
                        message = message,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
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
    isLastMessageByAuthor: Boolean
) {
    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier.then(spaceBetweenAuthors)) {
        Text(text = message.content)
    }
}

