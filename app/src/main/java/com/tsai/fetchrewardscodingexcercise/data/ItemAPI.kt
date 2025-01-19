package com.tsai.fetchrewardscodingexcercise.data

import com.tsai.fetchrewardscodingexcercise.data.model.ItemResponse
import retrofit2.http.GET

interface ItemAPI {
   @GET("hiring.json")
   suspend fun  fetchItems(): List<ItemResponse>
}
