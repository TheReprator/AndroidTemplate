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

package reprator.module.modulea.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView
import reprator.module.modulea.ModuleA
import reprator.module.modulea.R

object ModuleAScreen : KScreen<ModuleAScreen>() {

    override val layoutId: Int = R.layout.fragment_modulea
    override val viewClass: Class<*> = ModuleA::class.java

    val textView = KTextView { withId(R.id.moduleA_title) }
}
