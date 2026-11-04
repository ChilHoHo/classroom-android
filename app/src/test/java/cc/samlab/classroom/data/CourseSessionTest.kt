package cc.samlab.classroom.data

import java.time.Instant
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseSessionTest {
    @Test
    fun ongoingIncludesStartButExcludesEnd() {
        val start = Instant.parse("2026-11-04T04:00:00Z")
        val end = Instant.parse("2026-11-04T05:00:00Z")
        val session = CourseSession(1, start, end, "教室")
        assertFalse(session.isOngoing(start.minusMillis(1)))
        assertTrue(session.isOngoing(start))
        assertTrue(session.isOngoing(end.minusMillis(1)))
        assertFalse(session.isOngoing(end))
    }
}
