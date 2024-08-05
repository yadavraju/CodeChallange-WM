package com.raju.codechallange.di

import com.raju.codechallange.network.BASE_URL
import com.raju.codechallange.network.RetrofitBuilder
import com.raju.codechallange.network.api.CountryApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit = retrofitBuilder
        .build()

    @Provides
    @Singleton
    fun provideCountryApi(retrofit: Retrofit): CountryApi = retrofit.create(CountryApi::class.java)
}
