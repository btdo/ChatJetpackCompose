package com.example.composechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composechat.ui.ChatDrawer
import com.example.composechat.ui.theme.ComposeChatTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatApp(viewModel)
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
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = ChatScreen.fromRoute(backstackEntry.value?.destination?.route)

        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ChatDrawer(selected = currentScreen, onDestinationClicked = { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                })
            }) {
            NavHost(
                navController = navController,
                startDestination = ChatScreen.Conversation.route
            ) {
                chatScreens.forEach { screen ->
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

