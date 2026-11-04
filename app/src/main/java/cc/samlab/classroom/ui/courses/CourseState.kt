package cc.samlab.classroom.ui.courses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cc.samlab.classroom.data.ClassroomApi
import cc.samlab.classroom.data.CourseSchedule
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CourseState(private val api: ClassroomApi?) {
    var schedule by mutableStateOf(CourseSchedule())
        private set
    var loading by mutableStateOf(true)
        private set
    var failed by mutableStateOf(false)
        private set
    private val mutex = Mutex()

    suspend fun refresh() = mutex.withLock {
        loading = true
        failed = false
        try {
            schedule = api?.courseSchedule() ?: CourseSchedule()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            schedule = CourseSchedule()
            failed = true
        } finally {
            loading = false
        }
    }
}
