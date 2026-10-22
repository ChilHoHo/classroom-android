package cc.samlab.classroom.ui.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cc.samlab.classroom.R
import cc.samlab.classroom.data.normalizeBackendAddress

@Composable
fun BackendAddressDialog(
    address: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var draft by rememberSaveable { mutableStateOf(address) }
    var invalid by rememberSaveable { mutableStateOf(false) }
    fun save() {
        if (draft.isBlank()) {
            onSave("")
            return
        }
        val normalized = normalizeBackendAddress(draft)
        if (normalized == null) invalid = true else onSave(normalized)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.backend_address)) },
        text = {
            OutlinedTextField(
                value = draft,
                onValueChange = {
                    draft = it
                    invalid = false
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.server_address)) },
                placeholder = { Text(stringResource(R.string.address_example)) },
                singleLine = true,
                isError = invalid,
                supportingText = if (invalid) {
                    { Text(stringResource(R.string.invalid_address)) }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { save() }),
            )
        },
        confirmButton = {
            TextButton(onClick = { save() }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}
