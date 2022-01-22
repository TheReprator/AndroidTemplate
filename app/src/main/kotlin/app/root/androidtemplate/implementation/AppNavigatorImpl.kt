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

import androidx.navigation.NavController
import app.module.modulea.ModuleADirections
import app.template.navigation.AppNavigator
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppNavigatorImpl @Inject constructor() : AppNavigator {

    override fun navigateToLocationScreen(navController: NavController) {
        val direction = ModuleADirections.navigateToLocation()
        navController.navigate(direction)
    }

    override fun navigateToSettingScreen(navController: NavController) {
        val direction = ModuleADirections.navigateToSetting()
        navController.navigate(direction)
    }

    override fun navigateToNotification(navController: NavController) {
        val direction = ModuleADirections.navigateToNotification()
        navController.navigate(direction)
    }

    override fun navigateToBack(navController: NavController) {
        navController.navigateUp()
    }
}
