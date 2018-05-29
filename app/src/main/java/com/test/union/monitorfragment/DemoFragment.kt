package com.test.union.monitorfragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lib.monitorfragment.*
import kotlinx.android.synthetic.main.fragment_test.*

class DemoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 权限
        btn_sd.setOnClickListener { view ->
            maeRequestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA), object : MAEPermissionCallback {
                override fun onPermissionApplySuccess() {
                    Toast.makeText(activity, "fragment ===> 获取成功", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionApplyFailure(notGrantedPermissions: List<String>, shouldShowRequestPermissions: List<Boolean>) {
                    Toast.makeText(activity, "fragment ===> 获取失败！！", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // activityResult
        btn_result.setOnClickListener {
            maeStartActivityForResult(Intent(activity, DemoActivity::class.java), 20) { _, _, data ->
                Toast.makeText(activity, "fragment ====> " + data?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
            }
        }

        // lifecycle
//        btn_lifeCycle.setOnClickListener {
//            MAEMonitorFragment.getInstance(this)?.setLifecycleListener(object: MAELifecycleListener {
//                override fun onSaveInstanceState(outState: Bundle) {
//                    Toast.makeText(activity, "fragment ===> onSaveInstance()", Toast.LENGTH_SHORT).show()
//                }
//                override fun onCreate(savedInstanceState: Bundle?) {
//                    Toast.makeText(activity, "fragment ===> onCreate()", Toast.LENGTH_SHORT).show()
//                }
//                override fun onStop() {
//                    Toast.makeText(activity, "fragment ===> onStop()", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
    }
}