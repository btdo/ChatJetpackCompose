package com.example.composechat.ui

import android.util.Log
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
import com.example.composechat.profile.ProfileUiState
import com.example.composechat.profile.meProfile


@Composable
fun ProfileBody(profileScreenState: ProfileUiState, modifier: Modifier = Modifier) {
    Log.d("Profile", "recomposing $profileScreenState")
    if (profileScreenState is ProfileUiState.Loading) {
        LoadingScreen()
        return
    }
    val ui = (profileScreenState as ProfileUiState.ProfileState)
    val scrollState = rememberScrollState()
    Surface {
        Column(
            modifier
                .fillMaxSize()
                .padding(
                    start =
                    16.dp, end = 16.dp
                )
                .verticalScroll(scrollState)
        ) {
            ui.photo?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProfileNameAndPosition(
                profileScreenState = ui
            )
            ProfileProperty(
                title = stringResource(id = R.string.display_name),
                ui.displayName
            )
            ProfileProperty(title = stringResource(id = R.string.status), ui.status)
            ProfileProperty(
                title = stringResource(id = R.string.twitter),
                ui.twitter,
                true
            )
            ui.timeZone?.let {
                ProfileProperty(title = stringResource(id = R.string.timezone), it)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}

@Composable
@Preview
fun ProfileBodyPreview() {
    ProfileBody(profileScreenState = meProfile)
}

@Composable
fun ProfileNameAndPosition(profileScreenState: ProfileUiState.ProfileState) {
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



