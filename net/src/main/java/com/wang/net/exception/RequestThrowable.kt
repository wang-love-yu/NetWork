package com.wang.net.exception

import android.webkit.ConsoleMessage

class RequestThrowable(message: String?,var errorCode:String?=null) : Throwable(message) {
}