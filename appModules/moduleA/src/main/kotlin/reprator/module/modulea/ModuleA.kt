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

package reprator.module.modulea

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.template.base.util.isNull
import dagger.hilt.android.AndroidEntryPoint
import reprator.module.modulea.databinding.FragmentModuleaBinding

@AndroidEntryPoint
class ModuleA : Fragment(R.layout.fragment_modulea) {

     private var _binding: FragmentModuleaBinding? = null
      private val binding get() = _binding!!

      private val viewModel: ModuleAViewModal by viewModels()

      override fun onDestroyView() {
          _binding = null
          super.onDestroyView()
      }

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)

          _binding = FragmentModuleaBinding.bind(view).also {
              it.lifecycleOwner = viewLifecycleOwner
          }

          if (savedInstanceState.isNull()) {
              viewModel.fetchList()
          }
      }
}
