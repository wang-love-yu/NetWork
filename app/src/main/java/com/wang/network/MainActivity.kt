package com.wang.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wang.net.model.JudgeResource
import com.wang.net.viewModel.BaseViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var resultBean = ResultBean<String>()
        resultBean.data = "test error"
        resultBean.code = "!234"
        resultBean.isStatus = false
        BaseViewModel()
            .launch({
                Log.d(TAG, "onCreate: 111")
                resultBean
            }, {
                Log.d(TAG, "onCreate: ")
                if (it.isStatus) {
                    JudgeResource.success(it.data)
                } else {
                    JudgeResource.error(it.message)
                }
            }, {
                Log.d(TAG, "onCreate: wo sucess data = $it")
            }, {
                Log.d(TAG, "onCreate: wo fail error = ${it.message}")
            })
    }
}
