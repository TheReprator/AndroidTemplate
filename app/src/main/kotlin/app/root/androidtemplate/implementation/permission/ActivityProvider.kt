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

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import java.lang.ref.WeakReference

class ActivityProvider constructor(@ApplicationContext applicationContext: Application) :
    Application.ActivityLifecycleCallbacks {

    private val _activityFlow = MutableStateFlow(WeakReference<ComponentActivity>(null))
    val activityFlow = _activityFlow.asStateFlow()
        .distinctUntilChanged { old, new -> old.get() === new.get() }
        .filter {
            val activity = it.get()
            activity != null && activity.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)
        }
        .map { it.get()!! }

    val currentActivity
        get() = _activityFlow.value.get()
            ?.takeIf { it.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED) }

    init {
        (applicationContext).registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        (activity as? ComponentActivity)?.let {
            _activityFlow.value = WeakReference(it)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        (activity as? ComponentActivity)?.let {
            _activityFlow.value = WeakReference(it)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        (activity as? ComponentActivity)?.let {
            _activityFlow.value = WeakReference(it)
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}