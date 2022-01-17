@file:Suppress("DEPRECATION")

package app.module.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import app.module.setting.databinding.FragmentSettingBinding
import app.template.base_android.viewDelegation.viewBinding
import app.template.navigation.SettingNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingParentFragment : Fragment(R.layout.fragment_setting) {

    @Inject
    lateinit var settingNavigator: SettingNavigator

    private val binding by viewBinding(FragmentSettingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingToolbar.setOnClickListener {
            settingNavigator.navigateToBack(findNavController())
        }

        childFragmentManager.commit {
            replace(R.id.settingContainer, SettingPreferenceFragment())
        }
    }
}