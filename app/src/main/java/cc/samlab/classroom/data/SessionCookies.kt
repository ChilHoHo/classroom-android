package cc.samlab.classroom.data

import android.content.Context
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie
import java.net.URI
import org.json.JSONObject

internal class SessionCookies(context: Context, private val server: String) {
    private val storage = SessionStorage(context)
    private val manager = CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER)
    private val origin = URI(server)

    init {
        val saved = storage.read(server)
        if (saved != null) {
            try {
                val data = JSONObject(saved)
                val remaining = (data.getLong("expires") - System.currentTimeMillis()) / 1000
                if (remaining > 0) {
                    manager.cookieStore.add(origin, HttpCookie("connect.sid", data.getString("value")).apply {
                        path = "/"
                        secure = data.getBoolean("secure")
                        maxAge = remaining
                    })
                } else storage.clear(server)
            } catch (_: Exception) {
                storage.clear(server)
            }
        }
    }

    fun headers(uri: URI): Map<String, List<String>> = manager.get(uri, emptyMap())

    fun update(uri: URI, headers: Map<String?, List<String>>) {
        val acceptedHeaders = headers.filterKeys { it != null }.mapKeys { it.key!! }
        // Restored host-only cookies must not coexist with a renewed server cookie.
        if (acceptedHeaders.any { (name, values) ->
                name.equals("Set-Cookie", ignoreCase = true) &&
                    values.any { it.startsWith("connect.sid=", ignoreCase = true) }
            }) {
            manager.cookieStore.removeAll()
        }
        manager.put(uri, acceptedHeaders)
        if (acceptedHeaders.keys.none { it.equals("Set-Cookie", ignoreCase = true) }) return
        val cookie = manager.cookieStore.get(origin).firstOrNull { it.name == "connect.sid" }
        if (cookie == null || cookie.hasExpired()) {
            storage.clear(server)
        } else {
            val duration = if (cookie.maxAge > 0) cookie.maxAge else 7 * 24 * 60 * 60L
            storage.write(server, JSONObject().apply {
                put("value", cookie.value)
                put("secure", cookie.secure)
                put("expires", System.currentTimeMillis() + duration * 1000)
            }.toString())
        }
    }

    fun clear() {
        manager.cookieStore.removeAll()
        storage.clear(server)
    }
}
