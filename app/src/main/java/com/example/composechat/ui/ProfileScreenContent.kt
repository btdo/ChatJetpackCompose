package com.example.composechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composechat.R
import com.example.composechat.conversation.ProfileScreenState
import com.example.composechat.conversation.meProfile


@Composable
fun ProfileBody(profileScreenState: ProfileScreenState, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Surface {
        Column(
            modifier
                .fillMaxSize()
                .padding(
                    16.dp
                )
                .verticalScroll(scrollState)
        ) {
            profileScreenState.photo?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProfileNameAndPosition(profileScreenState = profileScreenState)
            ProfileProperty(
                title = stringResource(id = R.string.display_name),
                profileScreenState.displayName
            )
            ProfileProperty(title = stringResource(id = R.string.status), profileScreenState.status)
            ProfileProperty(
                title = stringResource(id = R.string.twitter),
                profileScreenState.twitter,
                true
            )
            profileScreenState.timeZone?.let {
                ProfileProperty(title = stringResource(id = R.string.timezone), it)
            }
        }
    }

}

@Composable
@Preview
fun ProfileBodyPreview() {
    ProfileBody(profileScreenState = meProfile)
}

@Composable
fun ProfileNameAndPosition(profileScreenState: ProfileScreenState) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = profileScreenState.name, style = MaterialTheme.typography.h4)
        Text(
            text = profileScreenState.position,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
fun ProfileProperty(title: String, content: String, isLink: Boolean = false) {
    Column {
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(top = 12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 12.dp)
        )
        val style = if (isLink) {
            MaterialTheme.typography.body1.copy(color = Color.Blue)
        } else {
            MaterialTheme.typography.body1
        }
        Text(text = content, style = style, modifier = Modifier.padding(top = 8.dp))
    }
}



