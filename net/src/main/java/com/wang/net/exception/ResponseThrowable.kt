package com.wang.net.exception

import android.webkit.ConsoleMessage

/***
 * @param message  异常原因
 * @param errorCode 异常code
 * @param others 自行设置
 *
 * **/
class ResponseThrowable(var message: String?, var errorCode: String? = null, var others: Any? = null) :
    Throwable(message) {
}