package com.raju.codechallange.data

import com.raju.codechallange.domain.repository.CountryRepository
import com.raju.codechallange.network.api.CountryApi
import com.raju.codechallange.network.model.Country
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(private val countryApi: CountryApi) :
    CountryRepository {

    override suspend fun getCountryList(): Result<List<Country>> {
        return try {
            val response = countryApi.getCountries()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}