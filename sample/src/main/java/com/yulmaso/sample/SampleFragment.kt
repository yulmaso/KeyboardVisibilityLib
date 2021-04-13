package com.yulmaso.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.yulmaso.keyboardvisibilitylibrary.setKeyboardVisibilityListener


/**
 *  Created by yulmaso
 *  Date: 13.04.21
 */
class SampleFragment: Fragment() {
    companion object {
        private const val SAMPLE_FRAGMENT_TAG = "SAMPLE_FRAGMENT_TAG"

        fun addToContainer(fm: FragmentManager, @IdRes container: Int) {
            fm.beginTransaction()
                .add(container, SampleFragment(), SAMPLE_FRAGMENT_TAG)
                .commit()
        }

        fun removeFromContainer(fm: FragmentManager) {
            fm.findFragmentByTag(SAMPLE_FRAGMENT_TAG)?.let {
                fm.beginTransaction().remove(it).commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val et = view.findViewById<EditText>(R.id.fragment_et)
        val btn = view.findViewById<Button>(R.id.to_activity_btn)

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.fragment_title)

        btn.setOnClickListener {
            removeFromContainer(requireFragmentManager())
        }

        setKeyboardVisibilityListener(viewLifecycleOwner) { visible ->
            if (visible) et.setHint(R.string.visible_hint)
            else {
                et.setHint(R.string.invisible_hint)
                et.clearFocus()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as SampleActivity).setActivityTitle()
    }
}