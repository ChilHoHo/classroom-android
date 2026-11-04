package cc.samlab.classroom.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import java.time.Instant
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CourseScheduleDeviceTest {
    @Test
    fun preservesWebDetailFieldsAndAllSelectedCourseSessions() {
        val schedule = CourseSchedule.parse(JSONObject("""
            {
              "classes": [
                {"id": 7, "status": "active", "course_name": "数据库系统",
                 "name": "软件班", "teacher_name": "教师", "term": "2026秋季",
                 "english_name": "Database Systems"},
                {"id": 8, "status": "archived", "course_name": "归档课程"}
              ],
              "sessions": [
                {"id": 2, "teaching_class_id": 7, "start_at": "2026-11-05T02:00:00Z",
                 "end_at": "2026-11-05T03:00:00Z", "location": "A201",
                 "change_reason": "教室调整", "original_start": "2026-11-04T02:00:00Z"},
                {"id": 1, "teaching_class_id": 7, "start_at": "2026-10-01T02:00:00Z",
                 "end_at": "2026-10-01T03:00:00Z", "location": "", "change_reason": null},
                {"id": 3, "teaching_class_id": 8}
              ]
            }
        """.trimIndent()))
        assertEquals("Database Systems", schedule.courses.single().englishName)
        assertEquals(listOf(1L, 2L), schedule.sessions.map { it.id })
        assertNull(schedule.sessions.first().changeReason)
        assertNull(schedule.sessions.first().originalStart)
        assertEquals("教室调整", schedule.sessions.last().changeReason)
        assertEquals(Instant.parse("2026-11-04T02:00:00Z"),
            schedule.sessions.last().originalStart)
    }
}
