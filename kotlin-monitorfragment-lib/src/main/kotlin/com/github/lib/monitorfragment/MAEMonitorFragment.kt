package com.github.lib.monitorfragment

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils

/**
 * 该Fragment提供生命周期监听和权限申请方法，因为二者实现原理一致，因此实现方法统一到该Fragment内
 */
internal class MAEMonitorFragment : Fragment() {

    /**
     * 生命周期回调，改成高阶函数
     */
    private var lifecycleListener: ((MAELifeCycleState, Bundle?) -> Unit)? = null
    /**
     * startActivityForResult回调
     */
    private var resultListener: ((Int, Int, Intent?) -> Unit)? = null
    private val request_code = 20
    private var reqPermissionBuilder: MAEReqPermissionBuilder? = null

    companion object {
        private const val MONITOR_FRAGMENT_TAG = "MAE_MONITOR_FRAGMENT_TAG"

        fun getInstance(fragment: android.support.v4.app.Fragment): MAEMonitorFragment? {
            return if (fragment?.isAdded) fragment?.activity?.let {
                getInstance(it)
            } else null
        }

        fun getInstance(fragment: Fragment): MAEMonitorFragment? {
            return if (fragment?.isAdded) fragment?.activity?.let {
                getInstance(it)
            } else null
        }

        fun <T : Activity> getInstance(activity: T): MAEMonitorFragment? {
            return if (!activity.isFinishing)
                (activity.fragmentManager.findFragmentByTag(MONITOR_FRAGMENT_TAG) as? MAEMonitorFragment)
                        ?: with(MAEMonitorFragment()) {
                            activity.fragmentManager.apply {
                                beginTransaction().add(this@with, MONITOR_FRAGMENT_TAG).commitAllowingStateLoss()
                                executePendingTransactions()
                            }
                            this@with
                        }
            else null
        }
    }

    fun setLifecycleListener(listener: (state: MAELifeCycleState, saveInstance: Bundle?) -> Unit) {
        this.lifecycleListener = listener
    }

    fun maeStartActivityForResult(intent: Intent, requestCode: Int, bundle: Bundle?, resultListener: ((Int, Int, Intent?) -> Unit)) {
        startActivityForResult(intent, requestCode, bundle)
        this.resultListener = resultListener
    }

    fun maeRequestPermission(permissions: Array<out String>, explain: String? = null, requestBuilder: MAEReqPermissionBuilder) {
        this.reqPermissionBuilder = requestBuilder
        val permissionsList = getPermissionsList(activity, permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsList.isNotEmpty()) {
            if (!activity.isFinishing) {
                explain?.let {
                    showExplainDialogAndRequestPermission(permissionsList, it)   // 为什么需要此权限
                } ?: requestPermissions(permissionsList.toTypedArray(), request_code)
            }
        } else {
            reqPermissionBuilder?.successClosure?.invoke()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        resultListener?.invoke(requestCode, resultCode, data)
    }

    private inline fun showExplainDialogAndRequestPermission(permissionsList: List<String>, explain: String) {
        AlertDialog.Builder(activity).apply {
            setMessage(explain)
            setCancelable(false)
            setPositiveButton(android.R.string.ok) { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toTypedArray(), request_code)
                } else {
                    reqPermissionBuilder?.successClosure?.invoke()
                }
            }.create().show()
        }
    }

    /**
     * 添加要求权限列表
     *
     * @param permissions 权限列表
     * @return true 表示需要解释，false不需要
     */
    private inline fun getPermissionsList(activity: Activity, permissions: Array<out String>): List<String> {
        return permissions.filter { ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED }.toList()
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != request_code) {
            return
        }

        var isGranted = true
        val notGrantedPermissions = mutableListOf<String>()
        val shouldShowRequestPermissions = mutableListOf<String>()
        permissions.forEachIndexed { index, s ->
            if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                isGranted = false
                notGrantedPermissions.add(s)

                // 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项 的 权限列表
                if (!shouldShowRequestPermissionRationale(s)) {
                    shouldShowRequestPermissions.add(s)
                }
            }
        }

        /* 国产手机无论是获取权限成功还是失败，这个结果都会有可能不可靠，需重新确认权限，详情参考
         * https://github.com/yanzhenjie/AndPermission/blob/master/README-CN.md#%E5%9B%BD%E4%BA%A7%E6%89%8B%E6%9C%BA%E9%80%82%E9%85%8D%E6%96%B9%E6%A1%88
         */
        reqPermissionBuilder?.let {
            if (isGranted && hasPermission(context, permissions)) {
                it.successClosure?.invoke()
            } else {
                if (hasPermission(context, permissions)) {
                    it.successClosure?.invoke()
                } else {
                    it.failedClosure?.invoke(notGrantedPermissions)

                    // 用户之前选择不再提醒时的权限列表
                    shouldShowRequestPermissions.let { shouldShow ->
                        if (shouldShowRequestPermissions.size > 0) {
                            it.shouldShowReqPermissionPermission?.invoke(shouldShow)
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断是否拥有权限
     * */
    private fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        permissions.forEachIndexed { _, s ->
            var result = ContextCompat.checkSelfPermission(context, s)
            if (result == PackageManager.PERMISSION_DENIED) {
                return false
            }
            val op = AppOpsManagerCompat.permissionToOp(s)
            if (!TextUtils.isEmpty(op)) {
                op?.let {
                    result = AppOpsManagerCompat.noteProxyOp(context, op, context.packageName)
                }
                if (result != AppOpsManagerCompat.MODE_ALLOWED) return false
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleListener?.invoke(MAELifeCycleState.ON_CREATE, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        lifecycleListener?.invoke(MAELifeCycleState.ON_START, null)
    }

    override fun onResume() {
        super.onResume()
        lifecycleListener?.invoke(MAELifeCycleState.ON_RESUME, null)
    }

    override fun onPause() {
        super.onPause()
        lifecycleListener?.invoke(MAELifeCycleState.ON_PAUSE, null)
    }

    override fun onStop() {
        super.onStop()
        lifecycleListener?.invoke(MAELifeCycleState.ON_STOP, null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleListener?.invoke(MAELifeCycleState.ON_SAVE_STATE, outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleListener?.invoke(MAELifeCycleState.ON_DESTROY, null)
    }
}

/**
 * 权限请求回调

interface MAEPermissionCallback {
fun onPermissionApplySuccess()
/**
 * @param notGrantedPermissions，       没有被用户允许的权限
 * @param shouldShowRequestPermissions 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
 * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。
*/
fun onPermissionApplyFailure(notGrantedPermissions: List<String>, shouldShowRequestPermissions: List<Boolean>)
}*/