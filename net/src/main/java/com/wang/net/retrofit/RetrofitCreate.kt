package com.wang.net.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.RuntimeException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

open class RetrofitCreate {
    private var mBaseUrl: String? = null
    private var mOkHttpClient: OkHttpClient? = null
    private var mConnectTimeout: Long = 10
    private var mReadTimeout: Long = 10
    private val mConverFactoryList: ArrayList<Converter.Factory> by lazy {
        ArrayList<Converter.Factory>()
    }
    private val mCallAdapterList: ArrayList<CallAdapter.Factory> by lazy {
        ArrayList<CallAdapter.Factory>()
    }
    private val mInterceptorList: ArrayList<Interceptor> by lazy {
        ArrayList<Interceptor>()
    }
    private val mNetWorkInterceptorList: ArrayList<Interceptor> by lazy {
        ArrayList<Interceptor>()
    }

    fun setBaseUrl(url: String): RetrofitCreate {
        mBaseUrl = url
        return this
    }

    //设置okhttp拦截器
    fun addInterceptor(vararg interceptor: Interceptor): RetrofitCreate {
        interceptor?.forEach {
            mInterceptorList.add(it)
        }
        return this
    }

    fun setConnectTimeout(time: Long): RetrofitCreate {
        mConnectTimeout = time
        return this
    }

    fun setReadTimeout(time: Long): RetrofitCreate {
        mReadTimeout = time
        return this
    }

    //设置okhttp网络拦截器
    fun addNetWorkInterceptor(vararg interceptor: Interceptor): RetrofitCreate {
        interceptor?.forEach {
            mInterceptorList.add(it)
        }
        return this
    }

    //设置ConverterFactory
    fun addConverFactory(vararg factorys: Converter.Factory): RetrofitCreate {
        factorys?.forEach {
            mConverFactoryList.add(it)
        }
        return this
    }

    //设置ConverterFactory
    fun addCallAdpterFactory(vararg factorys: CallAdapter.Factory): RetrofitCreate {
        factorys?.forEach {
            mCallAdapterList.add(it)
        }
        return this
    }

    /****
     * 如果设置okhttpClient，Interceptor connectTime...等都以设置的okhttp为准
     * **/
    fun setOkHttpClient(okHttpClient: OkHttpClient): RetrofitCreate {
        this.mOkHttpClient = okHttpClient
        return this
    }

    fun <T> create(serviceClass: Class<T>): T {
        checkConfig()
        if (mOkHttpClient == null) {
            var builder = getDefaultUnsafeOKHttpBuilder()
            //添加拦截器
            mInterceptorList?.forEach {
                builder.addInterceptor(it)
            }
            //添加网络拦截器
            mNetWorkInterceptorList?.forEach {
                builder.addNetworkInterceptor(it)
            }
            //设置超时时间
            builder.connectTimeout(mConnectTimeout, TimeUnit.SECONDS)
                .readTimeout(mReadTimeout, TimeUnit.SECONDS)
            mOkHttpClient = builder.build()
        }
        val builder = Retrofit.Builder()
        mConverFactoryList?.forEach {
            builder.addConverterFactory(it)
        }
        mCallAdapterList?.forEach {
            builder.addCallAdapterFactory(it)

        }
        builder
            .baseUrl(mBaseUrl)
            .client(mOkHttpClient)
        return builder.build().create(serviceClass)
    }

    private fun checkConfig() {
        if (mBaseUrl.isNullOrEmpty()) {
            throw RuntimeException("baseUrl not config")
        }
    }

    private fun getDefaultUnsafeOKHttpBuilder(): OkHttpClient.Builder {

        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val verifier = HostnameVerifier { hostname, session -> true }

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier(verifier)

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}