package com.nexon.darkwithlegacy

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : Activity() {
    private var _appCompatDelegate: AppCompatDelegate? = null
    val appCompatDelegate get() = _appCompatDelegate!!

    // AppCompat 분기 사용 여부
    // Activity -> true, AppCompatActivity -> false 로 지정
    private val appCompatFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
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
            }
        } else {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            findViewById<Button>(R.id.open_dialog_button).setOnClickListener {
                ThemeChangeDialogFragment().show(fragmentManager, "dialog")
            }
            findViewById<TextView>(R.id.resource_text).text = resources.getString(R.string.mode)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        _appCompatDelegate?.onPostCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        _appCompatDelegate?.onConfigurationChanged(newConfig)
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