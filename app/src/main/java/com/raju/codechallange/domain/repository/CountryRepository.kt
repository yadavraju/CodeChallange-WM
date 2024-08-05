package com.raju.codechallange.domain.repository

import com.raju.codechallange.network.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    suspend fun getCountryList(): Result<List<Country>>
}
