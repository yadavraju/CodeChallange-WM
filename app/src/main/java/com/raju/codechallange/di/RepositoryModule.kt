package com.raju.codechallange.di

import com.raju.codechallange.data.PersonRepositoryImpl
import com.raju.codechallange.domain.repository.PersonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun personRepository(personRepositoryImpl: PersonRepositoryImpl): PersonRepository

}
