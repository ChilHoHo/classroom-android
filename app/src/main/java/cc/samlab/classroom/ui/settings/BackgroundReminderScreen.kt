package cc.samlab.classroom.ui.settings

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cc.samlab.classroom.R
import cc.samlab.classroom.notifications.AttendanceReminderService
import cc.samlab.classroom.ui.components.PageLayout

@Composable
fun BackgroundReminderScreen(address: String, userId: Long?, onBack: () -> Unit, onLogin: () -> Unit) {
    val context = LocalContext.current
    val resources = LocalResources.current
    var running by remember { mutableStateOf(AttendanceReminderService.running) }
    var notificationsEnabled by remember { mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled()) }
    val lifecycle = (context as? LifecycleOwner)?.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                running = AttendanceReminderService.running
                notificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) }
    }
    PageLayout(stringResource(R.string.background_reminders), onBack) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(stringResource(R.string.background_reminders_description), style = MaterialTheme.typography.bodyLarge)
            Text(stringResource(if (running) R.string.background_reminders_active else R.string.background_reminders_inactive), style = MaterialTheme.typography.titleMedium)
            when {
                userId == null || address.isBlank() -> {
                    Text(stringResource(R.string.background_reminders_login))
                    TextButton(onClick = onLogin) { Text(stringResource(R.string.attendance_login)) }
                }
                !notificationsEnabled -> {
                    Text(stringResource(R.string.background_reminders_notifications_off))
                    TextButton(onClick = {
                        context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName))
                    }) { Text(stringResource(R.string.background_reminders_open_settings)) }
                }
                running -> Button(onClick = { AttendanceReminderService.stop(context); running = false }) {
                    Text(stringResource(R.string.background_reminders_stop))
                }
                else -> Button(onClick = {
                    try { AttendanceReminderService.start(context, address, userId); running = true }
                    catch (_: Exception) { Toast.makeText(context, resources.getString(R.string.background_reminders_failure), Toast.LENGTH_LONG).show() }
                }) { Text(stringResource(R.string.background_reminders_start)) }
            }
        }
    }
}
