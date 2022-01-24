package com.example.composechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composechat.conversation.BackPressHandler
import com.example.composechat.conversation.LocalBackPressedDispatcher
import com.example.composechat.ui.ChatDrawer
import com.example.composechat.ui.ConversationBody
import com.example.composechat.ui.ProfileBody
import com.example.composechat.ui.theme.ComposeChatTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                ChatApp(viewModel)
            }
        }
    }
}

@Composable
fun ChatApp(viewModel: MainViewModel) {
    ComposeChatTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }

        val conversation = ChatScreen.ConversationScreen(
            Icons.Filled.AccountBox,
            "Conversation",
            "conversation"
        ) {
            ConversationBody(ui = viewModel.conversationUI)
        }

        val profile = ChatScreen.ProfileScreen(Icons.Filled.Person, "Profile", "profile") {
            ProfileBody(viewModel.profileScreenState)
        }

        if (drawerState.isOpen) {
            BackPressHandler {
                scope.launch {
                    drawerState.close()
                }
            }
        }

        val screens = listOf(conversation, profile)
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = fromRoute(backstackEntry.value?.destination?.route, screens)

        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ChatDrawer(
                    screens = screens,
                    selected = currentScreen,
                    onDestinationClicked = { route ->
                        scope.launch {
                            drawerState.close()
                        }
                        if (route == conversation.route) {
                            navController.popBackStack(conversation.route, inclusive = false)
                        } else {
                            navController.navigate(route)
                        }
                    })
            }) {
            NavHost(
                navController = navController,
                startDestination = screens[0].route
            ) {
                screens.forEach { screen ->
                    composable(screen.route) {
                        ChatScreenScaffold(title = screen.title, body = {
                            screen.body()
                        }) {
                            openDrawer()
                        }
                    }
                }
            }
        }
    }
}

fun fromRoute(route: String?, screens: List<ChatScreen>): ChatScreen {
    if (route == null) {
        return screens[0]
    }
    var foundScreen: ChatScreen? = null
    screens.forEach {
        if (it.route == route) {
            foundScreen = it
        }
    }

    if (foundScreen == null) {
        throw IllegalArgumentException("Route $route is not recognized.")
    }
    return foundScreen!!
}


