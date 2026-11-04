package cc.samlab.classroom.ui.login

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cc.samlab.classroom.R

@Composable
fun StudentOnlyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.login_unavailable)) },
        text = { Text(stringResource(R.string.student_only_login)) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.confirm)) }
        },
    )
}
