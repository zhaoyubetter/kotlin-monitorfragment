package com.test.union.monitorfragment

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.lib.monitorfragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 权限
        btn_sd.setOnClickListener { view ->
            maeRequestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA), object : MAEPermissionCallback {
                override fun onPermissionApplySuccess() {
                    Toast.makeText(applicationContext, "获取成功", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionApplyFailure(notGrantedPermissions: List<String>, shouldShowRequestPermissions: List<Boolean>) {
                    Toast.makeText(applicationContext, "获取失败！！", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // activityResult
        btn_result.setOnClickListener {
            maeStartActivityForResult(Intent(applicationContext, DemoActivity::class.java), 20) { _, _, data ->
                Toast.makeText(applicationContext, data?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
            }
        }

        // lifecycle
        btn_lifeCycle.setOnClickListener {
            MAEMonitorFragment.getInstance(this)?.setLifecycleListener(object : MAELifecycleListener {
                override fun onSaveInstanceState(outState: Bundle) {
                    Toast.makeText(applicationContext, "onSaveInstance()", Toast.LENGTH_SHORT).show()
                }

                override fun onCreate(savedInstanceState: Bundle?) {
                    Toast.makeText(applicationContext, "onCreate()", Toast.LENGTH_SHORT).show()
                }

                override fun onStop() {
                    Toast.makeText(applicationContext, "onStop()", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // fragment

        btn_fragment.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.container, DemoFragment()).commit()
        }
    }
}
