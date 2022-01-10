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

package app.root.androidtemplate.implementation.permission

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.template.base.util.permission.PermissionDenied
import app.template.base.util.permission.PermissionGranted
import app.template.base.util.permission.PermissionManager
import app.template.base.util.permission.PermissionStatus
import dagger.hilt.android.qualifiers.ApplicationContext

class PermissionManagerImpl constructor(
    private val activityResultManager: ActivityResultManager,
    private val activityProvider: ActivityProvider,
    @ApplicationContext
    private val context: Context
) : PermissionManager {

    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PERMISSION_GRANTED
    }

    override suspend fun requestPermission(permission: String): PermissionStatus {
        val isGranted = activityResultManager.requestPermission(permission)
        return if (isGranted) {
            PermissionGranted
        } else {
            val shouldShowRationale = activityProvider.currentActivity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
            }
            PermissionDenied(shouldShowRationale ?: false)
        }
    }

    override suspend fun requestPermissions(vararg permissions: String): Map<String, PermissionStatus> {
        return activityResultManager.requestPermissions(*permissions)?.let { result ->
            permissions.associateWith { permission ->
                if (result[permission] == true) {
                    PermissionGranted
                } else {
                    val shouldShowRationale = activityProvider.currentActivity?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
                    }
                    PermissionDenied(shouldShowRationale ?: false)
                }
            }
        } ?: return permissions.associateWith {
            if (hasPermission(it)) PermissionGranted else PermissionDenied(shouldShowRationale = false)
        }
    }
}