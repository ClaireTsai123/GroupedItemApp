package com.tsai.fetchrewardscodingexcercise.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsai.fetchrewardscodingexcercise.data.ItemRepository
import com.tsai.fetchrewardscodingexcercise.data.model.ItemResponse
import com.tsai.fetchrewardscodingexcercise.data.transformer.ItemViewDataTransformer
import com.tsai.fetchrewardscodingexcercise.ui.model.ViewData
import com.tsai.fetchrewardscodingexcercise.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemViewModel : ViewModel() {
    private val _itemListLiveData: MutableLiveData<List<ViewData>> = MutableLiveData()
    val itemListLiveData: LiveData<List<ViewData>> = _itemListLiveData

    private val _errorMsgLiveData: MutableLiveData<String?> = MutableLiveData()
    val errorMsgLiveData: LiveData<String?> = _errorMsgLiveData
    private val repository = ItemRepository()
    private val transformer = ItemViewDataTransformer()

    fun fetchItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val result = repository.fetchItems()) {
                    is Result.Success -> {
                        val serverData: List<ItemResponse>? = result.data
                        val viewDataList = transformer.transform(serverData)
                        _itemListLiveData.postValue(viewDataList)
                    }

                    is Result.Error -> {
                        _errorMsgLiveData.postValue(result.message)
                    }
                }
            }
        }
    }
}
