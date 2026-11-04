package cc.samlab.classroom.ui.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cc.samlab.classroom.R
import cc.samlab.classroom.data.CourseSession
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val schoolZone = ZoneId.of("Asia/Shanghai")
private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(schoolZone)
private val timeFormat = DateTimeFormatter.ofPattern("HH:mm").withZone(schoolZone)

@Composable
fun CourseSessionCard(session: CourseSession) {
    Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(dateFormat.format(session.start), style = MaterialTheme.typography.titleMedium)
            Text("${timeFormat.format(session.start)}–${timeFormat.format(session.end)}")
            Text(stringResource(R.string.course_location,
                session.location.ifBlank { stringResource(R.string.not_configured) }))
            Text(stringResource(R.string.course_change_reason,
                session.changeReason ?: stringResource(R.string.no_course_changes)))
            if (session.changeReason != null && session.originalStart != null) {
                Text(stringResource(R.string.course_original_schedule,
                    "${dateFormat.format(session.originalStart)} ${timeFormat.format(session.originalStart)}"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
