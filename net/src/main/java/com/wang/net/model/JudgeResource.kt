package com.wang.net.model

/****
 * 这里用来对接口返回结果进行判断
 * **/
data class JudgeResource<T>(val status: Boolean, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?) = JudgeResource(true, data, null)

        fun <T> error(msg: String?, data: T? = null) = JudgeResource(false, data, msg)
    }
}
