package com.nexon.darkwithlegacy

import android.app.ActionBar
import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.TypedValue
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class MainActivity : Activity() {
    private var _appCompatDelegate: AppCompatDelegate? = null
    val appCompatDelegate get() = _appCompatDelegate!!

    // AppCompat 분기 사용 여부
    // Activity -> true, AppCompatActivity -> false 로 지정
    private val appCompatFlag = true
    private val clearFlag = false
    private val fixedModeFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (clearFlag) {
            clearMode()
            finish()
            return
        }

        if (fixedModeFlag) {
            fixedMode(Mode.DARK)
        }

        Log.d("LSYD", "onCreate: ${AppCompatDelegate.getDefaultNightMode()}")
        if (appCompatFlag) {
            _appCompatDelegate = AppCompatDelegate.create(this, null).apply {
                // this: AppCompatDelegate
                installViewFactory()
                onCreate(savedInstanceState)
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                findViewById<Button>(R.id.open_dialog_button)?.setOnClickListener {
                    ThemeChangeDialogFragment().show(fragmentManager, "dialog")
                }
                findViewById<TextView>(R.id.resource_text)?.text =
                    resources.getString(R.string.mode)
                findViewById<Button>(R.id.open_alert_button)?.setOnClickListener {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.mode)
                        .setPositiveButton(R.string.action_ok, null)
                        .create()
                        .show()
                }
            }
        } else {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            findViewById<Button>(R.id.open_dialog_button).setOnClickListener {
                ThemeChangeDialogFragment().show(fragmentManager, "dialog")
            }
            findViewById<TextView>(R.id.resource_text).text = resources.getString(R.string.mode)
            findViewById<Button>(R.id.open_alert_button)?.setOnClickListener {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.mode)
                    .setPositiveButton(R.string.action_ok, null)
                    .create()
                    .show()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.isLightTheme, typedValue, true)
            Log.d(
                "LSYD", "onCreate: ${
                    typedValue.data != 0
                }"
            )
        }
    }

    enum class Mode {
        DARK, LIGHT, AUTO
    }

    private fun clearMode() {
        setMode(Mode.AUTO)
        Log.e("LSYD", "CLEAR MODE COMPLETE")
    }

    private fun fixedMode(mode: Mode) {
        setMode(mode)
        Log.e("LSYD", "FIX MODE - $mode COMPLETE")
    }

    private fun setMode(mode: Mode) {
        data class ModeComponents(
            val isCustom: Boolean,
            val isDark: Boolean,
            val uiMode: Int,
            val appCompatMode: Int,
        )

        val (isCustom, isDark, uiMode, appCompatMode) = when (mode) {
            Mode.DARK -> ModeComponents(
                isCustom = true,
                isDark = true,
                uiMode = UiModeManager.MODE_NIGHT_YES,
                appCompatMode = AppCompatDelegate.MODE_NIGHT_YES
            )
            Mode.LIGHT -> ModeComponents(
                isCustom = true,
                isDark = false,
                uiMode = UiModeManager.MODE_NIGHT_NO,
                appCompatMode = AppCompatDelegate.MODE_NIGHT_NO
            )
            Mode.AUTO -> ModeComponents(
                isCustom = false,
                isDark = false,
                uiMode = UiModeManager.MODE_NIGHT_AUTO,
                appCompatMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
        getSharedPreferences(ThemeChangeDialogFragment.KEY, Context.MODE_PRIVATE)?.apply {
            edit {
                putBoolean(ThemeChangeDialogFragment.SETTINGS_DARK, isDark)
                putBoolean(ThemeChangeDialogFragment.SETTINGS_CUSTOM, isCustom)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager)?.apply {
                setApplicationNightMode(
                    uiMode
                )
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(
                appCompatMode
            )
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        _appCompatDelegate?.onPostCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("LSYD", "onConfigurationChanged start")
        super.onConfigurationChanged(newConfig)
        _appCompatDelegate?.onConfigurationChanged(newConfig)
        Log.d("LSYD", "onConfigurationChanged end")
    }

    override fun onStart() {
        super.onStart()
        _appCompatDelegate?.onStart()
    }

    override fun onStop() {
        super.onStop()
        _appCompatDelegate?.onStop()
    }

    override fun onPostResume() {
        super.onPostResume()
        _appCompatDelegate?.onPostResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _appCompatDelegate?.onSaveInstanceState(outState)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        _appCompatDelegate?.setTitle(title)
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        super.onTitleChanged(title, color)
        _appCompatDelegate?.setTitle(title)
    }

    override fun onDestroy() {
        super.onDestroy()
        _appCompatDelegate?.onDestroy()
        _appCompatDelegate = null
    }

    override fun addContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.addContentView(view, params)
        _appCompatDelegate?.addContentView(view, params)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        _appCompatDelegate?.setContentView(layoutResID)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        _appCompatDelegate?.setContentView(view)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        _appCompatDelegate?.setContentView(view, params)
    }

    override fun invalidateOptionsMenu() {
        super.invalidateOptionsMenu()
        _appCompatDelegate?.invalidateOptionsMenu()
    }

    override fun setActionBar(toolbar: Toolbar?) {
        if (toolbar == null) return
        throw Exception("Use AppCompatDelegate#setSupportActionBar")
    }

    override fun getActionBar(): ActionBar? {
        throw Exception("Use AppCompatDelegate#getSupportActionBar")
    }

    override fun getMenuInflater(): MenuInflater {
        return _appCompatDelegate?.menuInflater ?: super.getMenuInflater()
    }

    override fun <T : View?> findViewById(id: Int): T {
        return _appCompatDelegate?.findViewById<T>(id) ?: super.findViewById(id)
    }

    override fun setTheme(resid: Int) {
        _appCompatDelegate?.setTheme(resid) ?: super.setTheme(resid)
    }

    override fun attachBaseContext(newBase: Context) {
        _appCompatDelegate?.attachBaseContext2(newBase) ?: super.attachBaseContext(newBase)
    }
}