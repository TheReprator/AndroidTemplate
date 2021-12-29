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

package app.module.modulea

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.module.modulea.databinding.FragmentModuleaBinding
import app.template.base.util.isNull
import app.template.base_android.viewDelegation.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModuleA : Fragment(R.layout.fragment_modulea) {

    private val binding by viewBinding(FragmentModuleaBinding::bind)
    private val viewModel: ModuleAViewModal by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState.isNull()) {
            viewModel.fetchList()
        }
    }
}
