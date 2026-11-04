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
import cc.samlab.classroom.data.Course
import cc.samlab.classroom.data.CourseSession
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CourseCard(course: Course, session: CourseSession? = null, ongoing: Boolean = false) {
    Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (ongoing) Text(stringResource(R.string.course_ongoing),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge)
            Text(course.title, style = MaterialTheme.typography.titleLarge)
            Text(course.className, style = MaterialTheme.typography.bodyLarge)
            Text(stringResource(R.string.course_teacher, course.teacher))
            Text(course.term, color = MaterialTheme.colorScheme.onSurfaceVariant)
            session?.let {
                val format = DateTimeFormatter.ofPattern("MM月dd日 HH:mm")
                    .withZone(ZoneId.of("Asia/Shanghai"))
                val endFormat = DateTimeFormatter.ofPattern("HH:mm")
                    .withZone(ZoneId.of("Asia/Shanghai"))
                Text("${format.format(it.start)}–${endFormat.format(it.end)}")
                Text(it.location, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
