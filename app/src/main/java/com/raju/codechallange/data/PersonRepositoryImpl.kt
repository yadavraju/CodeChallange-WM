package com.raju.codechallange.data

import com.raju.codechallange.domain.repository.PersonRepository
import com.raju.codechallange.network.api.PersonApi
import com.raju.codechallange.network.model.PersonListResponse
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(private val personApi: PersonApi) :
    PersonRepository {

    override suspend fun getPersonList(): Result<PersonListResponse> {
        return try {
            val personList = personApi.getPersonList()
            Result.success(personList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}