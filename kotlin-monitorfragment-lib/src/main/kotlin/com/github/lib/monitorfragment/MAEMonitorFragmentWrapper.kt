package com.github.lib.monitorfragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle


/**
 * fragment
 */
fun Fragment.maeReqPermission(vararg permissions: String, explain: String? = null, closure: MAEReqPermissionBuilder.() -> Unit) {
    MAEMonitorFragment.getInstance(this)?.let {
        it.maeRequestPermission(permissions, explain, MAEReqPermissionBuilder().apply(closure))
    }
}

fun android.support.v4.app.Fragment.maeReqPermission(vararg permissions: String, explain: String? = null, closure: MAEReqPermissionBuilder.() -> Unit) {
    MAEMonitorFragment.getInstance(this)?.let {
        it.maeRequestPermission(permissions, explain, MAEReqPermissionBuilder().apply(closure))
    }
}

// 通过DSL配置
fun Activity.maeReqPermission( permissions: Array<out String>, explain: String? = null, closure: MAEReqPermissionBuilder.() -> Unit) {
    MAEMonitorFragment.getInstance(this)?.let {
        it.maeRequestPermission(permissions, explain, MAEReqPermissionBuilder().apply(closure))
    }
}

/**
 * 扩展方法, onActivityResult start
 */
fun Activity.maeStartForResult(intent: Intent, requestCode: Int, bundle: Bundle? = null, resultListener: (requestCode: Int, resultCode: Int, Intent?) -> Unit) {
    this?.let {
        MAEMonitorFragment.getInstance(this)?.maeStartActivityForResult(intent, requestCode, bundle, resultListener)
    }
}

fun Fragment.maeStartForResult(intent: Intent, requestCode: Int, bundle: Bundle? = null, resultListener: (requestCode: Int, resultCode: Int, Intent?) -> Unit) {
    this?.let {
        MAEMonitorFragment.getInstance(this)?.maeStartActivityForResult(intent, requestCode, bundle, resultListener)
    }
}

fun android.support.v4.app.Fragment.maeStartForResult(intent: Intent, requestCode: Int, bundle: Bundle? = null, resultListener: (requestCode: Int, resultCode: Int, Intent?) -> Unit) {
    this?.let {
        MAEMonitorFragment.getInstance(this)?.maeStartActivityForResult(intent, requestCode, bundle, resultListener)
    }
}

/**
 * lifeCycle
 */
fun Activity.maeLifeCycle(lifecycleListener: (MAELifeCycleState, Bundle?) -> Unit) {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(lifecycleListener)
}

fun Fragment.maeLifeCycle(lifecycleListener: (MAELifeCycleState, Bundle?) -> Unit) {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(lifecycleListener)
}

fun android.support.v4.app.Fragment.maeLifeCycle(lifecycleListener: (MAELifeCycleState, Bundle?) -> Unit) {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(lifecycleListener)
}


// 类
enum class MAELifeCycleState {
    ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, ON_SAVE_STATE
}

class MAEReqPermissionBuilder {
    /**
     * 请求成功
     */
    internal var successClosure: (() -> Unit)? = null
    /**
     * 请求失败，list为具体的权限
     */
    internal var failedClosure: ((List<String>) -> Unit)? = null
    /**
     * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，的列表
     */
    internal var shouldShowReqPermissionPermission: ((List<String>) -> Unit)? = null

    /**
     * 权限请求成功
     */
    fun success(closure: (() -> Unit)? = null) {
        this.successClosure = closure
    }

    fun failed(closure: ((List<String>) -> Unit)? = null) {
        this.failedClosure = closure
    }

    fun shouldShowReqPermission(closure: ((List<String>) -> Unit)? = null) {
        this.shouldShowReqPermissionPermission = closure
    }
}





