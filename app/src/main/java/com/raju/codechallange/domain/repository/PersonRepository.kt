package com.raju.codechallange.domain.repository

import com.raju.codechallange.network.model.PersonListResponse

interface PersonRepository {
    suspend fun getPersonList(): Result<PersonListResponse>
}