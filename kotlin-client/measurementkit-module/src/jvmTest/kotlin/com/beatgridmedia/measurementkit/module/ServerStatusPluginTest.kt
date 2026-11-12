package com.beatgridmedia.measurementkit.module

import com.beatgridmedia.measurementkit.module.authentication.LogoutMessage
import com.beatgridmedia.measurementkit.module.provider.DefaultPropertyStorageProvider
import com.beatgridmedia.measurementkit.module.system.ServerStatusMessage
import com.beatgridmedia.measurementkit.module.system.ServerStatusPlugin
import com.beatgridmedia.measurementkit.module.authentication.LogoutPlugin
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.beatkit.foundation.BeatKitModule
import org.beatkit.foundation.BeatKitRuntimeBase
import org.beatkit.foundation.platform.PlatformContext
import org.beatkit.foundation.plugin.BeatKitPlugin
import org.beatkit.foundation.provider.BeatKitProvider
import org.beatkit.module.core.provider.DefaultRestProvider
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Example of how plugins are tested: the runtime is created with the plugins under test and fake providers.
 *
 * Runs on the JVM target (`./gradlew jvmTest`) so no emulator / simulator is needed.
 */
class ServerStatusPluginTest {

    private fun runtime(vararg providers: BeatKitProvider) = BeatKitRuntimeBase(
        modules = listOf(object : BeatKitModule<MeasurementKitState> {
            override val plugins: Iterable<BeatKitPlugin<MeasurementKitState>> =
                listOf(ServerStatusPlugin(), LogoutPlugin())
            override val providers: Iterable<BeatKitProvider> = providers.toList()
        }),
        platformContext = PlatformContext(),
        initialState = MeasurementKitState.LoggedIn,
        callbackDispatcher = Dispatchers.Unconfined,
    ).apply { start() }

    private val restProvider = DefaultRestProvider("http://test") {
        HttpClient(MockEngine { request ->
            assertEquals("/api/v1/status", request.url.encodedPath)
            respond(
                content = """{"status":"UP","version":"1.2.3","serverTime":42}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        })
    }

    @Test
    fun serverStatusIsRetrieved() = runTest {
        val runtime = runtime(restProvider, DefaultPropertyStorageProvider())
        val status = runtime.send(ServerStatusMessage())
        assertEquals("UP", status.status)
        assertEquals("1.2.3", status.version)
    }

    @Test
    fun logoutChangesState() = runTest {
        val runtime = runtime(restProvider, DefaultPropertyStorageProvider())
        runtime.send(LogoutMessage())
        assertEquals(MeasurementKitState.LoggedOut, runtime.state)
    }
}
