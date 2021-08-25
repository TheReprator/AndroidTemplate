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

package app.template.base_android.util

import android.os.Build

val isAndroidPOrLater
    get() = Build.VERSION_CODES.P <= Build.VERSION.SDK_INT
val isAndroidOOrLater
    get() = Build.VERSION_CODES.O <= Build.VERSION.SDK_INT
val isAndroidLOrLater
    get() = Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT
val isAndroidMOrLater
    get() = Build.VERSION_CODES.M <= Build.VERSION.SDK_INT
val isAndroidNOrLater
    get() = Build.VERSION_CODES.N <= Build.VERSION.SDK_INT
val isAndroidKOrLater
    get() = Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT
val isAndroidJOrLater
    get() = Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT
