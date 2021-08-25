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

package reprator.module.modulea.util.test

import androidx.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import reprator.module.modulea.ModuleA
import reprator.module.modulea.dispatcherWithCustomBody
import reprator.module.modulea.util.CustomMockServer
import reprator.module.modulea.util.launchFragmentInHiltContainer
import reprator.module.modulea.util.screen.ModuleAScreen
import javax.inject.Inject

@HiltAndroidTest
class ModuleATest : TestCase() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    fun setUp() {
        mockWebServer = CustomMockServer().mockWebServer

        hiltRule.inject()

        IdlingRegistry.getInstance().register(okHttp3IdlingResource)

        mockWebServer.dispatcher = dispatcherWithCustomBody()

        launchFragmentInHiltContainer<ModuleA>()
    }

    @After
    fun cleanup() {
        mockWebServer.close()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun `load_item_successfully_onLaunch`() =
        run {
            step("1. Open App and show title") {
                testLogger.i("Main section")

                ModuleAScreen {
                    textView {
                        isVisible()
                        isCompletelyDisplayed()
                        hasText("ModuleA")
                    }
                }
            }
        }
}
