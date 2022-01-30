package com.example.composechat.conversation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.R
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun UserInputPreview() {
    UserInput(false, {})
}

@Composable
fun UserInput(
    isScrolling: Boolean,
    onMessageSend: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIcon by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val (text, setText) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    if (isScrolling) {
        focusManager.clearFocus()
        selectedIcon = InputSelector.NONE
    }

    Column(modifier = modifier) {
        UserInputText(text = text, onTextChanged = setText, {
            if (it) {
                selectedIcon = InputSelector.NONE
            }
        })
        UserInputSelector(onMessageSendClicked = {
            val time = SimpleDateFormat("HH:mm a").format(Date())
            onMessageSend(
                Message("aliconors", content = text, timestamp = time)
            )
            setText("")
        }, onIconSelected = {
            selectedIcon = it
        }, selected = selectedIcon, isSendEnabled = text.isNotEmpty())
        SelectorExpanded(currentSelector = selectedIcon) {
            selectedIcon = InputSelector.NONE
        }
    }
}

@Composable
fun SelectorExpanded(currentSelector: InputSelector, onDismiss: () -> Unit) {
    if (currentSelector == InputSelector.NONE)
        return

    val focusRequester = remember { FocusRequester() }
    // If the selector is shown, always request focus to trigger a TextField.onFocusChange.
    SideEffect {
        when (currentSelector) {
            InputSelector.EMOJI -> focusRequester.requestFocus()
            InputSelector.PICTURE -> focusRequester.requestFocus()
            InputSelector.MAP -> focusRequester.requestFocus()
            InputSelector.PHONE -> focusRequester.requestFocus()
            else -> {}
        }
    }

    when (currentSelector) {
        InputSelector.EMOJI -> FunctionalityNotAvailablePanel(focusRequester = focusRequester)
        InputSelector.DM -> NotAvailablePopup {
            onDismiss()
        }
        InputSelector.PICTURE -> FunctionalityNotAvailablePanel(focusRequester = focusRequester)
        InputSelector.MAP -> FunctionalityNotAvailablePanel(focusRequester = focusRequester)
        InputSelector.PHONE -> FunctionalityNotAvailablePanel(focusRequester = focusRequester)
        else -> {
            throw NotImplementedError()
        }
    }
}

@Composable
fun NotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available \uD83D\uDE48",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE")
            }
        }
    )
}

@Composable
fun FunctionalityNotAvailablePanel(focusRequester: FocusRequester) {
    Column(
        modifier = Modifier
            .focusRequester(focusRequester = focusRequester)
            .fillMaxWidth()
            .height(320.dp)
            .focusable(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.not_available),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(id = R.string.not_available_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun FunctionalityNotAvailablePanelPreview() {
    val focusRequester = remember { FocusRequester() }
    FunctionalityNotAvailablePanel(focusRequester = focusRequester)
}

@Preview
@Composable
fun UserInputSelectorPreview() {
    UserInputSelector(
        onMessageSendClicked = { /*TODO*/ },
        onIconSelected = {},
        selected = InputSelector.EMOJI, isSendEnabled = true
    )
}

@Preview
@Composable
fun UserInputSelectorPreview_Disabled() {
    UserInputSelector(
        onMessageSendClicked = { /*TODO*/ },
        onIconSelected = {},
        selected = InputSelector.EMOJI, isSendEnabled = false
    )
}

@Composable
fun UserInputSelector(
    modifier: Modifier = Modifier,
    onMessageSendClicked: () -> Unit,
    onIconSelected: (InputSelector) -> Unit,
    selected: InputSelector = InputSelector.NONE,
    isSendEnabled: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        InputSelectorButton(
            onClick = { onIconSelected(InputSelector.EMOJI) },
            icon = Icons.Outlined.Mood,
            description = stringResource(id = R.string.emoji_selector_bt_desc),
            selected = InputSelector.EMOJI == selected
        )
        InputSelectorButton(
            onClick = { onIconSelected(InputSelector.DM) },
            icon = Icons.Outlined.AlternateEmail,
            description = stringResource(id = R.string.dm_desc),
            selected = InputSelector.DM == selected
        )
        InputSelectorButton(
            onClick = { onIconSelected(InputSelector.PICTURE) },
            icon = Icons.Outlined.InsertPhoto,
            description = stringResource(id = R.string.attach_photo_desc),
            selected = InputSelector.PICTURE == selected
        )
        InputSelectorButton(
            onClick = { onIconSelected(InputSelector.MAP) },
            icon = Icons.Outlined.Place,
            description = stringResource(id = R.string.map_selector_desc),
            selected = InputSelector.MAP == selected
        )
        InputSelectorButton(
            onClick = { onIconSelected(InputSelector.PHONE) },
            icon = Icons.Outlined.Duo,
            description = stringResource(id = R.string.videochat_desc),
            selected = InputSelector.PHONE == selected
        )

        val disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        val buttonColors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = Color.Transparent,
            disabledContentColor = disabledContentColor
        )

        val border = if (isSendEnabled) {
            null
        } else {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }

        Button(
            modifier = Modifier.height(36.dp),
            enabled = isSendEnabled,
            onClick = onMessageSendClicked,
            colors = buttonColors,
            border = border
        ) {
            Text(stringResource(id = R.string.send))
        }
    }
}

@Composable
private fun InputSelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean
) {

    val background = if (selected) {
        Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.secondary)
    } else {
        Modifier
    }

    IconButton(
        onClick = onClick,
        modifier = background
    ) {
        val tint = if (selected) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.secondary
        }

        Icon(
            icon,
            tint = tint, modifier = Modifier.padding(16.dp), contentDescription = description
        )
    }
}


@Composable
fun UserInputText(
    text: String,
    onTextChanged: (String) -> Unit,
    onFocusChangeListener: (Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = Modifier
            .height(64.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged { state ->
                    onFocusChangeListener(state.isFocused)
                }
                .focusable(),
            placeholder = {
                Text(text = "Enter text")
            }
        )
    }

}

enum class InputSelector {
    NONE,
    MAP,
    DM,
    EMOJI,
    PHONE,
    PICTURE
}