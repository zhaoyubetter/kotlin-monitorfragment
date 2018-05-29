package com.github.lib.monitorfragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle

/**
 * 扩展方法, 权限请求 start
 */
fun Activity.maeRequestPermission(permissions: Array<out String>, maePermissionCallback: MAEPermissionCallback, explain: String? = null) {
    MAEMonitorFragment.getInstance(this)?.maeRequestPermission(permissions, maePermissionCallback, explain)
}

/**
 * fragment
 */
fun Fragment.maeRequestPermission(permissions: Array<out String>, maePermissionCallback: MAEPermissionCallback, explain: String? = null) {
    this?.let {
        it.activity.maeRequestPermission(permissions, maePermissionCallback, explain)
    }
}

fun android.support.v4.app.Fragment.maeRequestPermission(permissions: Array<out String>, maePermissionCallback: MAEPermissionCallback, explain: String? = null) {
    this?.let {
        it.activity?.maeRequestPermission(permissions, maePermissionCallback, explain)
    }
}

/**
 * 扩展方法, onActivityResult start
 */
fun Activity.maeStartActivityForResult(intent: Intent, requestCode: Int, bundle: Bundle? = null, resultListener: (requestCode: Int, resultCode: Int, Intent?) -> Unit) {
    this?.let {
        MAEMonitorFragment.getInstance(this)?.maeStartActivityForResult(intent, requestCode, bundle, resultListener)
    }
}

fun Fragment.maeStartActivityForResult(intent: Intent, requestCode: Int, bundle: Bundle? = null, resultListener: (requestCode: Int, resultCode: Int, Intent?) -> Unit) {
    this?.let {
        MAEMonitorFragment.getInstance(this)?.maeStartActivityForResult(intent, requestCode, bundle, resultListener)
    }
}

fun android.support.v4.app.Fragment.maeStartActivityForResult(intent: Intent, requestCode: Int, bundle: Bundle? = null, resultListener: (requestCode: Int, resultCode: Int, Intent?) -> Unit) {
    this?.let {
        MAEMonitorFragment.getInstance(this)?.maeStartActivityForResult(intent, requestCode, bundle, resultListener)
    }
}


/**
 * lifeCycle
 */
fun Activity.maeLifeCycleListener(lifecycleListener: (MAELifeCycleState, Bundle?) -> Unit) {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(lifecycleListener)
}

fun Fragment.maeLifeCycleListener(lifecycleListener: (MAELifeCycleState, Bundle?) -> Unit) {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(lifecycleListener)
}

fun android.support.v4.app.Fragment.maeLifeCycleListener(lifecycleListener: (MAELifeCycleState, Bundle?) -> Unit) {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(lifecycleListener)
}




