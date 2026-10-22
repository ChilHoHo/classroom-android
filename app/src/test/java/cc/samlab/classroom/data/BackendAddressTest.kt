package cc.samlab.classroom.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BackendAddressTest {
    @Test
    fun normalizesServerOrigin() {
        assertEquals(
            "http://192.168.1.10:3001",
            normalizeBackendAddress("  http://192.168.1.10:3001/  "),
        )
        assertEquals("https://classroom.example", normalizeBackendAddress("https://classroom.example"))
        assertEquals("http://[::1]:3001", normalizeBackendAddress("http://[::1]:3001"))
    }

    @Test
    fun rejectsInvalidOrAmbiguousAddresses() {
        listOf(
            "", "localhost:3001", "ftp://example.com", "https://example.com/api",
            "https://name:password@example.com", "https://example.com?token=secret",
            "https://example.com#fragment", "http://example.com:70000", "http://exa mple.com",
        ).forEach { assertNull(it, normalizeBackendAddress(it)) }
    }
}
