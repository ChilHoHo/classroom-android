package cc.samlab.classroom.data

import java.time.Instant
import org.json.JSONObject

data class Course(
    val id: Long,
    val title: String,
    val className: String,
    val teacher: String,
    val term: String,
)

data class CourseSession(
    val courseId: Long,
    val start: Instant,
    val end: Instant,
    val location: String,
) {
    fun isOngoing(now: Instant) = !now.isBefore(start) && now.isBefore(end)
}

data class CourseSchedule(
    val courses: List<Course> = emptyList(),
    val sessions: List<CourseSession> = emptyList(),
) {
    companion object {
        fun parse(json: JSONObject): CourseSchedule {
            val classes = json.getJSONArray("classes")
            val courses = buildList {
                for (index in 0 until classes.length()) {
                    val item = classes.getJSONObject(index)
                    if (item.getString("status") != "active") continue
                    add(Course(item.getLong("id"), item.getString("course_name"),
                        item.getString("name"), item.getString("teacher_name"),
                        item.getString("term")))
                }
            }
            val ids = courses.map { it.id }.toSet()
            val rows = json.getJSONArray("sessions")
            val sessions = buildList {
                for (index in 0 until rows.length()) {
                    val item = rows.getJSONObject(index)
                    val id = item.getLong("teaching_class_id")
                    if (id !in ids) continue
                    add(CourseSession(id, Instant.parse(item.getString("start_at")),
                        Instant.parse(item.getString("end_at")), item.getString("location")))
                }
            }
            return CourseSchedule(courses, sessions.sortedBy { it.start })
        }
    }
}
