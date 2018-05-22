package com.test.union.monitorfragment

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        btn_close.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply { putExtra("name", "better") })
            finish()
        }
    }


}
