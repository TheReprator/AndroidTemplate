package app.root.androidtemplate

import app.template.base.actions.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

class CurlLoggerInterceptor @Inject constructor(private val logger: Logger) : Interceptor {

    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        private const val SINGLE_DIVIDER = "────────────────────────────────────────────"
        private const val sTag = "App Postman CURL"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val curlCommandBuilder = StringBuilder("")
        // add cURL command
        curlCommandBuilder.append("cURL ")
        curlCommandBuilder.append("-X ")
        // add method
        curlCommandBuilder.append(request.method.uppercase() + " ")
        // adding headers
        for (headerName in request.headers.names()) {
            addHeader(curlCommandBuilder, headerName, request.headers[headerName])
        }

        // adding request body
        val requestBody = request.body
        if (request.body != null) {
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                addHeader(
                    curlCommandBuilder,
                    "Content-Type",
                    request.body?.contentType().toString()
                )
                charset = contentType.charset(UTF8)
                curlCommandBuilder.append(" -d '" + buffer.readString(charset!!) + "'")
            }
        }

        // add request URL
        curlCommandBuilder.append(" \"" + request.url.toString() + "\"")
        curlCommandBuilder.append(" -L")
        printLogs(request.url.toString(), curlCommandBuilder.toString())
        return chain.proceed(request)
    }

    private fun addHeader(
        curlCommandBuilder: StringBuilder,
        headerName: String,
        headerValue: String?
    ) {
        curlCommandBuilder.append("-H \"$headerName: $headerValue\" ")
    }

    private fun printLogs(url: String, msg: String?) {
        val logMsg = StringBuilder("\n")
        logMsg.append("\n")
        logMsg.append("URL: $url")
        logMsg.append("\n")
        logMsg.append(SINGLE_DIVIDER)
        logMsg.append("\n")
        logMsg.append(msg)
        logMsg.append(" ")
        logMsg.append(" \n")
        logMsg.append(SINGLE_DIVIDER)
        logMsg.append(" \n ")
        log(logMsg.toString())
    }

    private fun log(msg: String) {
        logger.d(sTag, msg)
    }
}