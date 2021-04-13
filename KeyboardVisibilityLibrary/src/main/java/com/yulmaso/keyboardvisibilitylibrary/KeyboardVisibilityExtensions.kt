package com.yulmaso.keyboardvisibilitylibrary

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 *  Created by yulmaso
 *  Date: 13.04.21
 */
interface KeyboardListener {
    fun remove()
}

fun Fragment.setKeyboardVisibilityListener(
    lifecycleOwner: LifecycleOwner,
    onVisibilityChanged: (visible: Boolean) -> Unit
): KeyboardListener {
    return requireActivity().setKeyboardVisibilityListener(lifecycleOwner.lifecycle, onVisibilityChanged)
}

fun Activity.setKeyboardVisibilityListener(
    lifecycle: Lifecycle,
    onVisibilityChanged: (visible: Boolean) -> Unit
): KeyboardListener {
    val mParentView: View = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

    val mOnGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var alreadyOpen = false
        private val defaultKeyboardHeightDP = 100
        private val EstimatedKeyboardDP = defaultKeyboardHeightDP +
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
        private val rect: Rect = Rect()
        override fun onGlobalLayout() {
            val estimatedKeyboardHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                EstimatedKeyboardDP.toFloat(),
                mParentView.resources.displayMetrics
            ).toInt()
            mParentView.getWindowVisibleDisplayFrame(rect)
            val heightDiff: Int = mParentView.rootView.height - (rect.bottom - rect.top)
            val isShown = heightDiff >= estimatedKeyboardHeight
            if (isShown == alreadyOpen) {
                return
            }
            alreadyOpen = isShown
            onVisibilityChanged.invoke(isShown)
        }
    }

    lateinit var mLifecycleObserver : LifecycleObserver

    val mKeyboardListener = object: KeyboardListener {
        override fun remove() {
            lifecycle.removeObserver(mLifecycleObserver)
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                mParentView.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
            else
                mParentView.viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener)
        }
    }

    mLifecycleObserver =  object: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            mKeyboardListener.remove()
        }
    }

    mParentView.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    lifecycle.addObserver(mLifecycleObserver)

    return mKeyboardListener
}