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

package app.template.base.actions.permission

interface PermissionManager {
    /**
     * Determine whether <em>you</em> have been granted a particular permission.
     *
     * @param permission The name of the permission being checked.
     * @return a [PermissionStatus] to indicate the current status.
     */
    fun hasPermission(permission: String): Boolean

    /**
     * Request a single permission.
     *
     * @param permission The name of the permission to be requested.
     * @return a [PermissionStatus] to indicate the current status.
     */
    suspend fun requestPermission(permission: String): PermissionStatus

    /**
     * Request multiple permission.
     *
     * @param permissions the names of permissions to be requested.
     * @return a [Map] containing the [PermissionStatus] of the request indexed by the
     * permission name
     */
    suspend fun requestPermissions(vararg permissions: String): Map<String, PermissionStatus>
}
