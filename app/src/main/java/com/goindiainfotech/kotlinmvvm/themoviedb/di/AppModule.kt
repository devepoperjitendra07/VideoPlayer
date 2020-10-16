package com.goindiainfotech.kotlinmvvm.themoviedb.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.goindiainfotech.kotlinmvvm.BuildConfig
import com.goindiainfotech.kotlinmvvm.themoviedb.data.tmdapi.NetworkService
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsBottom
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsTop
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.CheckNetwork
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstStrings
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.NoInternetException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSomeString(): String{
        return "It's some String!"
    }

    @Base_Url
    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Api_Key
    @Provides
    fun provideApiKey() = BuildConfig.API_KEY


    @Provides
    fun context(appInstance: Application): Context {
        return appInstance
    }

    @Provides
    @Singleton
    fun provideCheckNetwork(context: Context): CheckNetwork = CheckNetwork(context)

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }


    @Impl1
    @Provides
    @Singleton
    internal fun provideRetrofit(context: Context, checkNetwork: CheckNetwork, @Base_Url BASE_URL: String): Retrofit {

        val okHttpClient  = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(Cache(context.cacheDir, 10 * 1024 * 1024)) // 10 MB
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {

                    if(!checkNetwork.isNetworkConnected()) {
                        throw NoInternetException("Make sure you have an active data connection")

                    }
                    var original = chain.request()
                    // Customize the request
                    var request = original.newBuilder()
                        //.header("Content-Type", "application/json")
                        // .removeHeader("Pragma")
                        // .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                        .build()
                    // var response = chain.proceed(request)
                    // response.cacheResponse()
                    // Customize or return the response
                    return chain.proceed(request)
                }
            })
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            //.addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            // .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Impl2
    @Provides
    @Singleton
    internal fun provideRetrofitWC(context: Context, checkNetwork: CheckNetwork, @Base_Url BASE_URL: String): Retrofit {

        val okHttpClient  = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(Cache(context.cacheDir, 10 * 1024 * 1024)) // 10 MB
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {

                    var request = chain.request()
                    request =
                        if (checkNetwork.isNetworkConnected()) {
                            request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                                .build()
                        } else {
                            try {
                                request.newBuilder().header(
                                    "Cache-Control",
                                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                                ).build()
                            }catch(e:Exception){
                                throw NoInternetException("no data found")
                            }
                        }
                    return chain.proceed(request)
                }
            })
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            //.addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(@Impl2 retrofit: Retrofit): NetworkService = retrofit.create(NetworkService::class.java)

    @Provides
    @Named(ConstStrings.DEFAULT)
    fun provideGridLayoutManager(context: Context): GridLayoutManager = GridLayoutManager(context, 1)

    @Provides
    @Named(ConstStrings.HORIZONTAL)
    fun provideGridLayoutManagerHorizontal(context: Context): GridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)

    @Provides
    @Named(ConstStrings.COUNT3)
    fun provideGridLayoutManagerCount3(context: Context): GridLayoutManager = GridLayoutManager(context, 3)

  /*  @Provides
    fun provideFragmentDetailsTop(): FragmentDetailsTop = FragmentDetailsTop()

    @Provides
    fun provideFragmentDetailsBottom(): FragmentDetailsBottom = FragmentDetailsBottom()*/


}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Impl1

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Impl2

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Base_Url

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Api_Key