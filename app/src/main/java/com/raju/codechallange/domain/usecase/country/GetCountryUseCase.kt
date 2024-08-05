package com.raju.codechallange.domain.usecase.country

import com.raju.codechallange.domain.repository.CountryRepository
import com.raju.codechallange.network.model.Country
import javax.inject.Inject

class GetCountryUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(): Result<List<Country>> {
        return countryRepository.getCountryList()
    }
}
