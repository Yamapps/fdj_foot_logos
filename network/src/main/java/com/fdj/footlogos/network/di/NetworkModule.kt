package com.fdj.footlogos.network.di

import android.content.Context
import android.net.ConnectivityManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.fdj.footlogos.network.BuildConfig
import com.fdj.footlogos.network.NetworkDataSource
import com.fdj.footlogos.network.retrofit.RetrofitNetworkApi
import com.fdj.footlogos.network.retrofit.RetrofitNetworkDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    companion object {

        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        fun okHttpCallFactory(): Call.Factory {
            return OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            if (BuildConfig.DEBUG) {
                                setLevel(HttpLoggingInterceptor.Level.BODY)
                            }
                        },
                )
                .build()
        }

        @Provides
        @Singleton
        fun imageLoader(
            okHttpCallFactory: dagger.Lazy<Call.Factory>,
            @ApplicationContext application: Context,
        ): ImageLoader {
            return ImageLoader.Builder(application)
                .callFactory { okHttpCallFactory.get() }
                .components { add(SvgDecoder.Factory()) }
                .respectCacheHeaders(false)
                .apply {
                    if (BuildConfig.DEBUG) {
                        logger(DebugLogger())
                    }
                }
                .build()
        }

        @Provides
        fun provideConnectivityManager(@ApplicationContext appContext: Context): ConnectivityManager {
            return appContext.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        }

        @Provides
        fun provideRetrofitApi(networkJson: Json, okhttpCallFactory: dagger.Lazy<Call.Factory>): RetrofitNetworkApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory { okhttpCallFactory.get().newCall(it) }
                .addConverterFactory(
                    networkJson.asConverterFactory("application/json".toMediaType()),
                )
                .build()
                .create(RetrofitNetworkApi::class.java)
        }

        private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/50130162/"
    }

    @Binds
    internal abstract fun bindsNetworkDataSource(
        retrofitNetworkDataSource: RetrofitNetworkDataSource
    ): NetworkDataSource

}
