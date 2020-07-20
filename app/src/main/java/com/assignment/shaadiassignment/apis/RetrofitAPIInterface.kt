package com.assignment.shaadiassignment.apis

import com.assignment.shaadiassignment.model.PeopleMatchesListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPIInterface {
    @GET(ApiURLs.PEOPLE_MATCHES)
    fun getAllMatchedPeople(@Query("results") totalResults: String): Call<PeopleMatchesListModel>
}