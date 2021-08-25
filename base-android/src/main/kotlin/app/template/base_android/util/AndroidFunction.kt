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

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment

@get:RestrictTo(RestrictTo.Scope.LIBRARY)
internal inline val isMainThread: Boolean
    get() = Looper.myLooper() == Looper.getMainLooper()

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun checkIsMainThread() = check(isMainThread)

fun <T, U> getCallingObjectForActivityFragment(
    referenceType: U,
    callActivity: Activity.() -> T,
    callFragment: Fragment.() -> T
): T where U : Context {
    return when (referenceType) {
        is Activity -> callActivity(referenceType)
        is Fragment -> callFragment(referenceType)
        else -> {
            throw IllegalArgumentException("Caller must be an Activity or a Fragment.")
        }
    }
}

fun getContext(
    param: Any
): Context {
    return when (param) {
        is Activity -> param
        is Fragment -> param.requireContext()
        else -> {
            throw IllegalArgumentException("Caller must be an Activity or a Fragment.")
        }
    }
}

fun isAllPermissionsGranted(grantResults: IntArray): Boolean {
    var isGranted = true

    for (grantResult in grantResults) {
        isGranted = grantResult == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            break
        }
    }

    return isGranted
}
