package cc.samlab.classroom.data

import android.content.Context
import java.net.URI

class BackendSettings(context: Context) {
    private val preferences = context.getSharedPreferences("backend", Context.MODE_PRIVATE)

    var address: String
        get() = preferences.getString("address", "").orEmpty()
        set(value) {
            preferences.edit().putString("address", value).apply()
        }
}

fun normalizeBackendAddress(input: String): String? {
    val value = input.trim().trimEnd('/')
    return try {
        val uri = URI(value)
        value.takeIf {
            uri.scheme in setOf("http", "https") &&
                !uri.host.isNullOrBlank() &&
                uri.rawUserInfo == null &&
                uri.rawQuery == null &&
                uri.rawFragment == null &&
                (uri.port == -1 || uri.port in 1..65535) &&
                uri.rawPath.orEmpty().isEmpty()
        }
    } catch (_: Exception) {
        null
    }
}
