package cc.samlab.classroom.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cc.samlab.classroom.data.BackendSettings
import cc.samlab.classroom.data.ClassroomApi
import cc.samlab.classroom.ui.home.HomeScreen
import cc.samlab.classroom.ui.login.AccountScreen
import cc.samlab.classroom.ui.login.LoginScreen
import cc.samlab.classroom.ui.login.LoginState
import cc.samlab.classroom.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

private enum class Destination { Home, Settings, Login }

@Composable
fun ClassroomApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settings = remember { BackendSettings(context.applicationContext) }
    var address by remember { mutableStateOf(settings.address) }
    var destination by rememberSaveable { mutableStateOf(Destination.Home) }
    val api = remember(address) {
        address.takeIf { it.isNotEmpty() }?.let { ClassroomApi(context.applicationContext, it) }
    }
    val auth = remember(api) { LoginState(context.applicationContext, api, scope) }
    val goHome = { if (!auth.busy) destination = Destination.Home }

    LaunchedEffect(auth, destination) {
        if (destination == Destination.Home) auth.restore()
    }
    DisposableEffect(auth, context) {
        val lifecycle = (context as? LifecycleOwner)?.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && auth.user != null) {
                scope.launch { auth.restore() }
            }
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) }
    }
    BackHandler(destination != Destination.Home) { goHome() }

    Surface(Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = destination,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "page",
        ) { page ->
            when (page) {
                Destination.Home -> HomeScreen(
                    onLogin = { destination = Destination.Login },
                    onSettings = { destination = Destination.Settings },
                    username = auth.user?.username,
                )
                Destination.Settings -> SettingsScreen(
                    address = address,
                    onBack = goHome,
                    onSaveAddress = {
                        settings.address = it
                        address = it
                    },
                )
                Destination.Login -> if (auth.user != null) {
                    AccountScreen(auth, goHome)
                } else {
                    LoginScreen(
                        hasBackend = api != null,
                        state = auth,
                        onBack = goHome,
                        onSettings = { destination = Destination.Settings },
                        onSuccess = { destination = Destination.Home },
                    )
                }
            }
        }
    }
}
