package com.enciyo.pinbook.ui.books

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.enciyo.pinbook.R
import com.enciyo.pinbook.databinding.ItemCategoryBinding
import com.enciyo.pinbook.domain.model.Category
import com.enciyo.pinbook.utils.inflater
import com.enciyo.pinbook.utils.textWithFadeAnimation


class CategoryAdapter :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(categoryDiffUtil) {

    var listener: ((CategoryAdapterEvent) -> Unit)? = null
    var selectedItem: Category? = null

    inner class CategoryViewHolder(private val mBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun initView(item: Category?) = with(mBinding) {
            item?.let { item ->
                mBinding.textViewCategoryName.textWithFadeAnimation = item.name
                mBinding.textViewCategoryName.setBackgroundColor(
                    if (item.isSelected)
                        mBinding.root.context.resources.getColor(R.color.blue_500)
                    else
                        mBinding.root.context.resources.getColor(R.color.grey_800)
                )
                root.setOnClickListener {
                    onClickedItem(item)
                }
            }
        }

        fun updateView(item: Category?) {
            item?.let { item ->
                mBinding.textViewCategoryName.setBackgroundColor(
                    if (item.isSelected)
                        mBinding.root.context.resources.getColor(R.color.blue_500)
                    else
                        mBinding.root.context.resources.getColor(R.color.grey_800)
                )
            }
        }


        private fun onClickedItem(item: Category) {
            if (checkSameItemSelected(item)) {
                removeOldSelectedItem()
                listener?.invoke(CategoryAdapterEvent.NonSelected)
            } else {
                removeOldSelectedItem()
                updateUIBySelectedItem(item)
                listener?.invoke(CategoryAdapterEvent.OnSelected(item))
            }
        }

        private fun checkSameItemSelected(item: Category) =
            selectedItem?.id == item.id

        private fun removeOldSelectedItem() {
            val selectedIndex = currentList.indexOfFirst { it.id == selectedItem?.id }
            if (selectedIndex != -1) {
                currentList[selectedIndex].isSelected = false
                notifyItemChanged(selectedIndex,"1")
                selectedItem = null
            }
        }

        private fun updateUIBySelectedItem(item: Category) {
            item.isSelected = item.isSelected.not()
            notifyItemChanged(currentList.indexOf(item),"1")
            selectedItem = item
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val bind = ItemCategoryBinding.inflate(parent.inflater, parent, false)
        return CategoryViewHolder(bind)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.initView(getItem(position))
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty().not()) {
            holder.updateView(getItem(position))
        } else super.onBindViewHolder(holder, position, payloads)
    }


    companion object {

        val categoryDiffUtil = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
                oldItem == newItem
        }

        sealed class CategoryAdapterEvent {
            data class OnSelected(val category: Category) : CategoryAdapterEvent()
            object NonSelected : CategoryAdapterEvent()
        }

    }


}