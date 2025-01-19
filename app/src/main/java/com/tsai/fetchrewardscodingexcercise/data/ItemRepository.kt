package com.tsai.fetchrewardscodingexcercise.data

import com.tsai.fetchrewardscodingexcercise.data.model.ItemResponse
import com.tsai.fetchrewardscodingexcercise.data.model.Result

class ItemRepository {
    private val itemAPI = RetrofitInstance.itemAPI
    suspend fun fetchItems(): Result<List<ItemResponse>> {
        return try {
            val items = itemAPI.fetchItems()
            Result.Success(items)
        } catch (e: Exception) {
            Result.Error("Failed to fetch items : $e")
        }
    }
}
