package com.tsai.fetchrewardscodingexcercise.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tsai.fetchrewardscodingexcercise.R
import com.tsai.fetchrewardscodingexcercise.databinding.ItemBodyBinding
import com.tsai.fetchrewardscodingexcercise.databinding.ItemHeaderBinding
import com.tsai.fetchrewardscodingexcercise.ui.model.HeaderViewData
import com.tsai.fetchrewardscodingexcercise.ui.model.ItemContentViewData
import com.tsai.fetchrewardscodingexcercise.ui.model.ViewData
import com.tsai.fetchrewardscodingexcercise.ui.util.GenericDiffCallback

class GroupedItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val groupedItems = mutableListOf<ViewData>()
    private val expandedHeaders = mutableMapOf<Int, Boolean>()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    inner class HeaderViewHolder(val headerBinding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(headerBinding.root) {
    }

    inner class ItemViewHolder(val itemBinding: ItemBodyBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun getItemViewType(position: Int): Int {
        val viewData = groupedItems[position]
        return if (viewData is HeaderViewData) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding =
                    ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }

            VIEW_TYPE_ITEM -> {
                val binding =
                    ItemBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return groupedItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val data = groupedItems[position]
        val context = holder.itemView.context
        if (viewType == VIEW_TYPE_HEADER) {
            val viewData = data as HeaderViewData
            val headerViewHolder = holder as HeaderViewHolder
            headerViewHolder.headerBinding.header.text =
                context.getString(R.string.item_header_template, viewData.listId.toString())

            // Set up click listener to toggle expand/collapse
            headerViewHolder.itemView.setOnClickListener {
                val listId = viewData.listId
                val isExpanded = expandedHeaders[listId] ?: false
                expandedHeaders[listId] = !isExpanded
                updateFlattenedList()
            }
        } else if (viewType == VIEW_TYPE_ITEM) {
            val viewData = data as? ItemContentViewData
            val itemViewHolder = holder as ItemViewHolder
            itemViewHolder.itemBinding.content.text =
                context.getString(R.string.item_content_template, viewData?.name, viewData?.id)
        }
    }

    private fun updateFlattenedList() {
        val newFlattenedList = getFlattenedList()
        setData(newFlattenedList)
    }

    fun setInitialDataSet(newGroupedItems: List<ViewData>) {
        // we show all items by default (all header has expanded by default)
        newGroupedItems.filterIsInstance<HeaderViewData>().forEach {
            expandedHeaders[it.listId] = true
        }
        setData(newGroupedItems)
    }

    private fun setData(newGroupedItems: List<ViewData>) {
        val diffCallback = GenericDiffCallback(
            oldList = groupedItems,
            newList = newGroupedItems,
            areItemsSame = { oldItem, newItem -> oldItem == newItem },
            areContentsSame = { oldItem, newItem -> oldItem == newItem }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        groupedItems.clear()
        groupedItems.addAll(newGroupedItems)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getFlattenedList(): List<ViewData> {
        val flattenedList = mutableListOf<ViewData>()
        groupedItems.forEach { viewData ->
            if (viewData is HeaderViewData) {
                flattenedList.add(viewData)
                if (expandedHeaders[viewData.listId] == true) {
                    flattenedList.addAll(viewData.items)
                }
            }
        }
        return flattenedList
    }
}
