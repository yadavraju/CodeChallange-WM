package com.raju.codechallange.di

import com.raju.codechallange.data.CountryRepositoryImpl
import com.raju.codechallange.domain.repository.CountryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun countryRepository(emojiRepositoryImpl: CountryRepositoryImpl): CountryRepository

}
