package com.wang.net.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wang.net.exception.RequestThrowable
import com.wang.net.model.JudgeResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val TAG = "BaseViewModel"
    //通过改变值，视图层监听开始，结束
    val mIsLoadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /***
     * @param execute 执行方法体 在里面执行网络请求
     *@param judgment 判断方法体，在里面对返回结果进行判断
     * @param success 执行成功方法体 接口数据返回成功执行结果
     * @param failed 执行失败体，接口返回失败时 这里区分两大类， 1. 接口网络请求"成功"但返回的false 2. 接口没请求成功 比如超时，网络异常等
     * @param isShowLoading 是否修改mIsLoadingLiveData 通过改变值，视图层可以监听请求开始，结束 （可以用来显示隐藏加载框)
     * ***/

    open fun <T, V> launch(
        execute: suspend () -> T,
        judgment: suspend (T) -> JudgeResource<V>,
        success: suspend (V?) -> Unit,
        failed: (suspend (RequestThrowable) -> Unit) = {
            Log.d(TAG, "launch: e= ${it.message}")
        }
        , isShowLoading: Boolean = true
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                updateLoadingLiveData(isShowLoading, true)
                var result = execute()
                var juide = judgment(result)
                if (juide.status) {
                    success(juide.data)
                    updateLoadingLiveData(isShowLoading, false)
                } else {
                    failed(RequestThrowable(juide.message, juide.errCode))
                    updateLoadingLiveData(isShowLoading, false)

                }
            } catch (e: RequestThrowable) {
                failed(e)
                mIsLoadingLiveData.value = false
                updateLoadingLiveData(isShowLoading, false)

            }
        }
    }

    /***
     * @param isShowLoading 是否展示loading
     * @param status 状态值
     * **/
    private fun updateLoadingLiveData(isShowLoading: Boolean, status: Boolean) {
        if (!isShowLoading) {
            return
        }
        if (isShowLoading) {
            mIsLoadingLiveData.value = status
        }

    }

}