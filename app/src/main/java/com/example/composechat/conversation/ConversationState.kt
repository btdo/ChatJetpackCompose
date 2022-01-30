package com.example.composechat.conversation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import com.example.composechat.R


sealed class ConversationUiState {
    object Loading : ConversationUiState()
    data class ConversationState(
        val channelName: String,
        val channelMembers: Int,
        val initialMessages: List<Message>
    ) : ConversationUiState() {
        var messages = mutableStateListOf(*initialMessages.toTypedArray())
            private set

        fun addMessage(message: Message) {
            messages.add(0, message)
        }
    }
}


@Immutable
data class Message(
    val author: String,
    val content: String,
    val timestamp: String,
    val image: Int? = null,
    val authorImage: Int = if (author == "me") R.drawable.ali else R.drawable.someone_else
)

val initialMessages = listOf(
    Message(
        "aliconors",
        "Check it out!",
        "8:07 PM"
    ),
    Message(
        "aliconors",
        "Thank you!",
        "8:06 PM",
        R.drawable.sticker
    ),
    Message(
        "taylor",
        "You can use all the same stuff",
        "8:05 PM"
    ),
    Message(
        "taylor",
        "@aliconors Take a look at the `Flow.collectAsState()` APIs",
        "8:05 PM"
    ),
    Message(
        "jglenn",
        "Compose newbie as well, have you looked at the JetNews sample? Most blog posts end up " +
                "out of date pretty fast but this sample is always up to date and deals with async " +
                "data loading (it's faked but the same idea applies) \uD83D\uDC49" +
                "https://github.com/android/compose-samples/tree/master/JetNews",
        "8:04 PM"
    ),
    Message(
        "aliconors",
        "Compose newbie: I’ve scourged the internet for tutorials about async data loading " +
                "but haven’t found any good ones. What’s the recommended way to load async " +
                "data and emit composable widgets?",
        "8:03 PM"
    )
)