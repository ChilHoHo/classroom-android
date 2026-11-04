package cc.samlab.classroom.ui.courses

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cc.samlab.classroom.R

@Composable
fun CourseRefreshButton(enabled: Boolean, onRefresh: () -> Unit) {
    FilledTonalIconButton(
        onClick = onRefresh,
        enabled = enabled,
        shape = CircleShape,
    ) {
        Icon(painterResource(R.drawable.ic_refresh),
            contentDescription = stringResource(R.string.refresh))
    }
}
