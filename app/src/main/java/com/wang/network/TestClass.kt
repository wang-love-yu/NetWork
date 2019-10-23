package com.wang.network

import retrofit2.http.GET

interface TestClass {

    @GET("")
    fun test()
}