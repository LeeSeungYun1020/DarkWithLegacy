package com.nexon.darkwithlegacy

import android.app.DialogFragment
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.nexon.darkwithlegacy.databinding.FragmentThemeChangeBinding

class ThemeChangeDialogFragment : DialogFragment() {
    private var _binding: FragmentThemeChangeBinding? = null
    private val binding get() = _binding!!

    // 버전 테스트 분기 사용 여부
    private val versionFlag = true

    companion object {
        const val KEY = "key"
        const val SETTINGS_CUSTOM = "custom"
        const val SETTINGS_DARK = "dark"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentThemeChangeBinding.inflate(inflater, container, false)
        activity?.getSharedPreferences(KEY, Context.MODE_PRIVATE)?.apply {
            val isCustom = getBoolean(SETTINGS_CUSTOM, false)
            val isDark = getBoolean(SETTINGS_DARK, false)

            binding.customSwitch.isChecked = isCustom
            binding.darkSwitch.isEnabled = isCustom
            binding.darkSwitch.isChecked = isDark
        }
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            customSwitch.setOnCheckedChangeListener { _, isChecked ->
                onCustomSwitchCheckedChange(isChecked)
            }
            darkSwitch.setOnCheckedChangeListener { _, isChecked ->
                onDarkSwitchCheckedChange(isChecked)
            }
            closeButton.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun onCustomSwitchCheckedChange(isChecked: Boolean) {
        var isDark = false
        activity?.getSharedPreferences(KEY, Context.MODE_PRIVATE)?.apply {
            isDark = getBoolean(SETTINGS_DARK, false)
            edit {
                putBoolean(SETTINGS_CUSTOM, isChecked)
            }
        }
        binding.darkSwitch.isEnabled = isChecked
        if (versionFlag && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (activity?.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager)?.apply {
                setApplicationNightMode(
                    when {
                        !isChecked -> UiModeManager.MODE_NIGHT_AUTO
                        isDark -> UiModeManager.MODE_NIGHT_YES
                        else -> UiModeManager.MODE_NIGHT_NO
                    }
                )
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(
                when {
                    !isChecked -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    isDark -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }

    private fun onDarkSwitchCheckedChange(isChecked: Boolean) {
        activity?.getSharedPreferences(KEY, Context.MODE_PRIVATE)?.edit(true) {
            putBoolean(SETTINGS_DARK, isChecked)
        }

        if (versionFlag && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (activity?.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager)?.apply {
                setApplicationNightMode(
                    when {
                        isChecked -> UiModeManager.MODE_NIGHT_YES
                        else -> UiModeManager.MODE_NIGHT_NO
                    }
                )
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}