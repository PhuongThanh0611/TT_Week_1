package com.example.tt_week_1
import com.example.tt_week_1.ext.ConstExt.Companion.Base_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryMovie {
    companion object {
        private var retrofit: Retrofit? = null
        private var builder: Retrofit.Builder = Retrofit.Builder().baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        private val httpClient = OkHttpClient.Builder()
        fun <S> createService(serviceClass: Class<S>): S {
            return createService(serviceClass, null)
        }

        private fun <S> createService(serviceClass: Class<S>, authToken: Map<String, String>?): S {
            if (authToken != null) {
                val interceptor = AuthenticationInterceptor(authToken)
                if (!httpClient.interceptors().contains(interceptor)) {
                    httpClient.addInterceptor(interceptor)
                    builder.client(httpClient.build())
                    retrofit = builder.build()
                }
            }
            retrofit = builder.build()
            return retrofit!!.create(serviceClass)
        }

    }

    class AuthenticationInterceptor(private val authToken: Map<String, String>) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val builder = original.newBuilder()
            for (key in authToken.keys) {
                builder.header(key, authToken.getValue(key))
            }
            val request = builder.build()
            return chain.proceed(request)
        }
    }
}