package com.wang.net.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/***
 * 协程Retofit生成器
 * **/
abstract class CoroutinesRetrofitCreate {


    private  val TAG = "CoroutinesRetrofitCreate";
    private fun getUnsafeOKHttp(): OkHttpClient {

        //val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d(TAG, "log: message= $message") })
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
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
               // .addInterceptor(HeaderIntercept())
               // .addInterceptor(httpLoggingInterceptor)
               // .addInterceptor(NetWorkInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
    abstract fun setBaseUrl()

    fun <T> create(serviceClass: Class<T>){
       /* val coroutinesRetrofitCreate = CoroutinesRetrofitCreate()
        val httpClient = coroutinesRetrofitCreate.getUnsafeOKHttp()
        val builder = Retrofit.Builder()
            //.addConverterFactory(ScalarsConverterFactory.create())
            // .addConverterFactory(CustomGsonConverterFactory.create())
     //       .baseUrl(UrlUtils.BASEURL)
            .client(httpClient)
        val retrofit = builder
            .build()*/

    }
    companion object{



    }

}