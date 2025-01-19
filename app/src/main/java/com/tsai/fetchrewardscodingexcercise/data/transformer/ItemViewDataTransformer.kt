package com.tsai.fetchrewardscodingexcercise.data.transformer

import com.tsai.fetchrewardscodingexcercise.data.model.ItemResponse
import com.tsai.fetchrewardscodingexcercise.ui.model.HeaderViewData
import com.tsai.fetchrewardscodingexcercise.ui.model.ItemContentViewData
import com.tsai.fetchrewardscodingexcercise.ui.model.ViewData

class ItemViewDataTransformer : Transformer<List<ItemResponse>?, List<ViewData>> {
    override fun transform(source: List<ItemResponse>?): List<ViewData> {
        val viewDataList = mutableListOf<ViewData>()
        if (source.isNullOrEmpty()) return viewDataList

        val items: Map<Int, List<ItemResponse>> = source.filter { !it.name.isNullOrBlank() }
            .sortedWith(compareBy<ItemResponse> { it.listId }.thenBy { it.name })
            .groupBy { it.listId }
        for (entry in items.entries) {
            val itemContentList = transformItemsViewData(entry.value)
            val header = transformHeaderViewData(entry.key, itemContentList)
            viewDataList.add(header)
            viewDataList.addAll(itemContentList)
        }
        return viewDataList
    }

    private fun transformHeaderViewData(
        listId: Int,
        items: List<ItemContentViewData>
    ): HeaderViewData {
        return HeaderViewData(listId, items = items)
    }

    private fun transformItemsViewData(items: List<ItemResponse>): List<ItemContentViewData> {
        return items.map { response ->
            ItemContentViewData(
                id = response.id,
                name = response.name.orEmpty()
            )
        }
    }
}
