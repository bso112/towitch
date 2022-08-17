package com.manta.tiwtch.di

import android.content.SharedPreferences
import com.manta.tiwtch.BuildConfig
import com.manta.tiwtch.data.AddTokenInterceptor
import com.manta.tiwtch.data.TwitchApiService
import com.manta.tiwtch.data.TwitchIdService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchIdRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchApiRetrofit


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient
            .Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AddTokenInterceptor(sharedPreferences))
            .build()
    }

    @TwitchIdRetrofit
    @Provides
    @Singleton
    fun provideTwitchIdRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.TWITCH_ID)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @TwitchApiRetrofit
    @Provides
    @Singleton
    fun provideTwitchApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.TWITCH_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideTwitchIdService(@TwitchIdRetrofit retrofit: Retrofit): TwitchIdService {
        return retrofit.create(TwitchIdService::class.java)
    }

    @Provides
    @Singleton
    fun provideTwitchApiService(@TwitchApiRetrofit retrofit: Retrofit): TwitchApiService {
        return retrofit.create(TwitchApiService::class.java)
    }


}