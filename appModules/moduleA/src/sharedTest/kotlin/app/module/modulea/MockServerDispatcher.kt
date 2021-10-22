/*
 * Copyright 2021 Vikram LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.module.modulea

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.SocketPolicy
import java.util.concurrent.TimeUnit

fun dispatcherWithCustomBody() = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        with(request.path) {
            val response = MockResponse().setResponseCode(200)
            return when {
                this?.contains("/repositories") == true ->
                    response
                        .setBody(FileReader.readTestResourceFile("response_one.json"))
                else ->
                    throw Exception("Wrong path")
            }
        }
    }
}

fun dispatcherWithEmptyBody() = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        with(request.path) {
            val response = MockResponse().setResponseCode(200)
            return when {
                this?.contains("/repositories") == true ->
                    response
                        .setBody(
                            "[]"
                        )
                else ->
                    throw Exception("Wrong path")
            }
        }
    }
}

fun dispatcherWithErrorTimeOut() = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse()
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)
            .throttleBody(1, 2, TimeUnit.SECONDS)
    }
}
