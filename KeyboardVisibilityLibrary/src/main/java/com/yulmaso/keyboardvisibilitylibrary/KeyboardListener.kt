package com.yulmaso.keyboardvisibilitylibrary

import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

/**
 *  Created by yulmaso
 *  Date: 13.04.21
 */
class KeyboardListener(
    private val mLifecycle: Lifecycle,
    private val mParentView: View,
    private val mLifecycleObserver: LifecycleObserver,
    private val mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener
) {
    fun removeKeyboardListener() {
        mLifecycle.removeObserver(mLifecycleObserver)
        mParentView.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }
}