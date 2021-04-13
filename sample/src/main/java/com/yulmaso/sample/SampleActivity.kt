package com.yulmaso.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.yulmaso.keyboardvisibilitylibrary.setKeyboardVisibilityListener

class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        setActivityTitle()

        findViewById<Button>(R.id.to_fragment_btn).setOnClickListener {
            SampleFragment.addToContainer(supportFragmentManager, android.R.id.content)
        }

        val et = findViewById<EditText>(R.id.activity_et)

        setKeyboardVisibilityListener(lifecycle) { visible ->
            if (visible) et.setHint(R.string.visible_hint)
            else {
                et.setHint(R.string.invisible_hint)
                et.clearFocus()
            }
        }
    }

    fun setActivityTitle() {
        supportActionBar?.title = getString(R.string.activity_title)
    }
}