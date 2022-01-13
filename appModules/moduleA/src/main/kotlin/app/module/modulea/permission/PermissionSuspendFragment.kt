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

package app.module.modulea.permission

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.module.modulea.R
import app.module.modulea.databinding.FragmentPermissionBinding
import app.template.base_android.viewDelegation.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PermissionSuspendFragment :Fragment(R.layout.fragment_permission) {

    private val binding by viewBinding(FragmentPermissionBinding::bind)
    private val viewModel: PermissionSuspendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toggleButton.setOnClickListener {
            viewModel.toggleState()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            observeViewState(binding)
        }
    }

    private fun CoroutineScope.observeViewState(binding: FragmentPermissionBinding) {
        viewModel.viewState
            .onEach { viewState ->
                binding.toggleButton.text = if (viewState.isObservingLocation) "Stop" else "Start"
                if (viewState.isObservingLocation) {
                    viewState.currentLocation?.let {
                        binding.currentLocation.text =
                            "Current Location ${it.latitude},${it.longitude}"
                    } ?: run {
                        binding.currentLocation.text = "Getting your location!"
                    }
                } else {
                    binding.currentLocation.text = ""
                }
            }
            .launchIn(this)
    }
}