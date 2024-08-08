package com.raju.codechallange.network.api

import com.raju.codechallange.network.model.PersonListResponse
import retrofit2.http.GET

interface PersonApi {

    //https://gist.githubusercontent.com/
    @GET("russellbstephens/9e528b12fd1a45a7ff4e4691adcddf10/raw/5ec8ce76460e8f29c9b0f99f3bf3450b06696482/people.json")
    suspend fun getPersonList(): PersonListResponse
}