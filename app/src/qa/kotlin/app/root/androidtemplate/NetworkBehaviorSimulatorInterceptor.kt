package app.root.androidtemplate

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

/**
 * An OkHttp [Interceptor] which simulates network conditions using a
 * retrofit-mock [NetworkBehavior]. This is useful for simulating delays, errors, etc on a real
 * network endpoint.
 */
internal class NetworkBehaviorSimulatorInterceptor(
    private val networkBehavior: NetworkBehavior,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        networkBehavior.delaySleep()
        return when {
            networkBehavior.calculateIsError() -> {
                networkBehavior.createErrorResponse()
                    .raw()
                    .newBuilder()
                    .body("Mock Retrofit Error".toResponseBody("text/plain".toMediaTypeOrNull()))
                    .build()
            }
            networkBehavior.calculateIsFailure() -> throw networkBehavior.failureException()
            else -> chain.proceed(chain.request())
        }
    }
}

private fun NetworkBehavior.delaySleep(): Boolean {
    val sleepMs = calculateDelay(TimeUnit.MILLISECONDS)
    if (sleepMs > 0) {
        try {
            Thread.sleep(sleepMs)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
        }
    }
    return true
}
