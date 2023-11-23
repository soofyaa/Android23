package com.itis.android23.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itis.android23.R
import com.itis.android23.data.NotificationSettings
import com.itis.android23.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _viewBinding: FragmentSettingsBinding? = null
    private val viewBinding: FragmentSettingsBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applySettingsToViews()

        viewBinding.saveButton.setOnClickListener {
            saveSettings()
        }
    }

    private fun applySettingsToViews() {
        viewBinding.importanceRadioGroup.check(NotificationSettings.importanceCheckedId)
        viewBinding.privacyRadioGroup.check(NotificationSettings.privacyCheckedId)
        viewBinding.detailedTextCheckBox.isChecked = NotificationSettings.isDetailedTextChecked
        viewBinding.showButtonsCheckBox.isChecked = NotificationSettings.isShowButtonsChecked
    }

    private fun saveSettings() {
        NotificationSettings.importanceCheckedId = viewBinding.importanceRadioGroup.checkedRadioButtonId
        NotificationSettings.privacyCheckedId = viewBinding.privacyRadioGroup.checkedRadioButtonId
        NotificationSettings.isDetailedTextChecked = viewBinding.detailedTextCheckBox.isChecked
        NotificationSettings.isShowButtonsChecked = viewBinding.showButtonsCheckBox.isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}

