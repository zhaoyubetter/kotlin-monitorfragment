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
            maeReqPermission(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION) {
                success {
                    Toast.makeText(context, "获取成功", Toast.LENGTH_SHORT).show()
                }
                failed {
                    Toast.makeText(context, "获取失败！！", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // activityResult
        btn_result.setOnClickListener {
            maeStartForResult(Intent(activity, DemoActivity::class.java), 20) { _, _, data ->
                Toast.makeText(activity, "fragment ====> " + data?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
            }
        }

        // 生命周期
        btn_lifeCycle.setOnClickListener {
            maeLifeCycle { state, _ ->
                when (state) {
                    MAELifeCycleState.ON_STOP ->
                        Toast.makeText(context, "onStop", Toast.LENGTH_SHORT).show()
                    MAELifeCycleState.ON_DESTROY ->
                        Toast.makeText(context, "onDestroy", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}