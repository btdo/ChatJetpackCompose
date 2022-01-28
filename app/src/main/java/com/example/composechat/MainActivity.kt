package com.example.composechat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composechat.conversation.BackPressHandler
import com.example.composechat.conversation.LocalBackPressedDispatcher
import com.example.composechat.profile.meProfile
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
    Log.d("MainActivity", "recomposing $viewModel")
    ComposeChatTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            Log.d("MainActivity", "launched effect recomposing $viewModel")
            viewModel.getConversationState()
        }
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }

        val profileUiState = viewModel.profileUiState.collectAsState()
        val profile = ChatScreen.ProfileScreen(
            Icons.Filled.Person,
            stringResource(id = R.string.profile_screen)
        ) {
            ProfileBody(profileUiState.value)
        }

        val conversationUiState = viewModel.conversationState.collectAsState()
        val conversation = ChatScreen.ConversationScreen(
            Icons.Filled.AccountBox,
            stringResource(id = R.string.conversation_screen)
        ) {
            ConversationBody(conUiState = conversationUiState.value, onProfileClicked = {
                navController.navigate("${profile.route}/${it}")
            })
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
                        when (fromRoute(route, screens)) {
                            is ChatScreen.ProfileScreen -> navController.navigate("${profile.route}/${meProfile.displayName}")
                            else -> navController.popBackStack(
                                conversation.route,
                                inclusive = false
                            )
                        }
                    })
            }) {
            NavHost(
                navController = navController,
                startDestination = screens[0].route
            ) {
                composable("${profile.route}/{name}", listOf(navArgument("name") {
                    type = NavType.StringType
                })) { entry ->
                    Log.d("MainActivity", "profile recomposing")
                    val name = requireNotNull(entry.arguments?.getString("name"))
                    ChatScreenScaffold(title = profile.title, body = {
                        viewModel.getProfile(name)
                        profile.body()
                    }) {
                        openDrawer()
                    }
                }

                composable(conversation.route) {
                    ChatScreenScaffold(title = conversation.title, body = {
                        conversation.body()
                    }) {
                        openDrawer()
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
        if (it.route == route.substringBefore("/")) {
            foundScreen = it
        }
    }

    if (foundScreen == null) {
        throw IllegalArgumentException("Route $route is not recognized.")
    }
    return foundScreen!!
}


