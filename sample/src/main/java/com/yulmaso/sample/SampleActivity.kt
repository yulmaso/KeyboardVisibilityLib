package com.yulmaso.sample

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yulmaso.keyboardvisibilitylibrary.setKeyboardVisibilityListener

class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        setActivityTitle()

        val et = findViewById<EditText>(R.id.et)
        val tv = findViewById<TextView>(R.id.tv)

        val keyboardListener = setKeyboardVisibilityListener(lifecycle) { visible ->
            if (visible) tv.setText(R.string.visible_msg)
            else {
                tv.setText(R.string.invisible_msg)
                et.clearFocus()
            }
        }

        findViewById<Button>(R.id.to_fragment_btn).setOnClickListener {
            SampleFragment.addToContainer(supportFragmentManager, android.R.id.content)
        }

        findViewById<Button>(R.id.remove_btn).setOnClickListener {
            keyboardListener.remove()
        }
    }

    fun setActivityTitle() {
        supportActionBar?.title = getString(R.string.activity_title)
    }
}