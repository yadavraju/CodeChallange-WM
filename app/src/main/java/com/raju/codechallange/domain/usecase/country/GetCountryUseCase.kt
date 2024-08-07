package com.raju.codechallange.domain.usecase.country

import com.raju.codechallange.domain.repository.CountryRepository
import com.raju.codechallange.domain.usecase.BaseUseCase
import com.raju.codechallange.network.model.Country
import javax.inject.Inject

class GetCountryUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) : BaseUseCase<Void, Result<List<Country>>>() {

   override suspend fun execute(params: Void?): Result<List<Country>> {
        return countryRepository.getCountryList()
    }
}
