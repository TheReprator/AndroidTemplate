/*
 * Copyright 2021
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

package app.root.androidtemplate.implementation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import app.template.base.util.permission.PermissionHelper
import app.template.base_android.util.isAndroidMOrLater
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PermissionHelperImpl @Inject constructor(private val context: Context) : PermissionHelper {

    override fun hasPermissions(permission: List<String>): Boolean {
        return hasPermissions(*permission.toTypedArray())
    }

    override fun hasPermissions(permission: String): Boolean {
        return hasPermissions(*arrayOf(permission))
    }

    override fun hasPermissions(vararg perms: String): Boolean {
        if (isAndroidMOrLater)
            for (perm in perms)
                if (ContextCompat.checkSelfPermission(
                        context,
                        perm
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    return false
        return true
    }
}
