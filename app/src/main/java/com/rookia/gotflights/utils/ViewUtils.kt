package com.rookia.gotflights.utils

import android.app.Activity
import java.lang.ref.WeakReference


@Suppress("unused")
interface ViewUtils {

    fun showAlertDialog(
        activity: WeakReference<Activity>, allowCancelWhenTouchingOutside: Boolean,
        title: String? = null, message: String? = null,
        positiveButton: String? = null, callbackPositive: ((m: Unit?) -> Any?)? = null,
        negativeButton: String? = null, callbackNegative: ((m: Unit?) -> Any?)? = null
    )

    fun needToSetThemeAsDark(backgroundColor: Int): Boolean
}