package com.test.union.monitorfragment

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.lib.monitorfragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 权限
        btn_sd.setOnClickListener {
            maeReqPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), "摄像头与更好的位置") {
                success {
                    Toast.makeText(applicationContext, "获取成功", Toast.LENGTH_SHORT).show()
                }
                failed {
                    Toast.makeText(applicationContext, "获取失败！！", Toast.LENGTH_SHORT).show()
                }
                // 用户选择不再询问，执行以下
                shouldShowReqPermission { it ->
                    Log.e("better", it.toString())
                }
            }
        }

        // activityResult
        btn_result.setOnClickListener {
            maeStartForResult(Intent(applicationContext, DemoActivity::class.java), 20) { _, _, data ->
                Toast.makeText(applicationContext, data?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
            }
        }

        // lifecycle
        btn_lifeCycle.setOnClickListener {
            maeLifeCycle { state, _ ->
                when (state) {
                    MAELifeCycleState.ON_STOP ->
                        Toast.makeText(applicationContext, "onStop", Toast.LENGTH_SHORT).show()
                    MAELifeCycleState.ON_DESTROY ->
                        Toast.makeText(applicationContext, "onDestroy", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // fragment
        btn_fragment.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.container, DemoFragment()).commit()
        }
    }
}
