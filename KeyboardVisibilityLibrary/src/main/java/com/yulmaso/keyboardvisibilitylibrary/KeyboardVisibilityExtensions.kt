package com.yulmaso.keyboardvisibilitylibrary

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 *  Created by yulmaso
 *  Date: 13.04.21
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun Fragment.setKeyboardVisibilityListener(
    lifecycleOwner: LifecycleOwner,
    onVisibilityChanged: (visible: Boolean) -> Unit
): KeyboardListener {
    return requireActivity().setKeyboardVisibilityListener(lifecycleOwner.lifecycle, onVisibilityChanged)
}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun Activity.setKeyboardVisibilityListener(
    lifecycle: Lifecycle,
    onVisibilityChanged: (visible: Boolean) -> Unit
): KeyboardListener {
    val parentView: View = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    val onGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var alreadyOpen = false
        private val defaultKeyboardHeightDP = 100
        private val EstimatedKeyboardDP = defaultKeyboardHeightDP +
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
        private val rect: Rect = Rect()
        override fun onGlobalLayout() {
            val estimatedKeyboardHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                EstimatedKeyboardDP.toFloat(),
                parentView.resources.displayMetrics
            ).toInt()
            parentView.getWindowVisibleDisplayFrame(rect)
            val heightDiff: Int = parentView.rootView.height - (rect.bottom - rect.top)
            val isShown = heightDiff >= estimatedKeyboardHeight
            if (isShown == alreadyOpen) {
                return
            }
            alreadyOpen = isShown
            onVisibilityChanged(isShown)
        }
    }
    val lifecycleObserver =  object: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            lifecycle.removeObserver(this)
            parentView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    parentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    lifecycle.addObserver(lifecycleObserver)
    return KeyboardListener(lifecycle, parentView, lifecycleObserver, onGlobalLayoutListener)
}