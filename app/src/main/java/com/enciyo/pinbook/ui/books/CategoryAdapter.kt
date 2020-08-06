package com.enciyo.pinbook.ui.books

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.enciyo.pinbook.databinding.ItemCategoryBinding
import com.enciyo.pinbook.ui.books.models.CategoryModel
import com.enciyo.pinbook.utils.inflater
import com.enciyo.pinbook.utils.textWithFadeAnimation


class CategoryAdapter : ListAdapter<CategoryModel,CategoryAdapter.CategoryViewHolder>(categoryDiffUtil){

  class CategoryViewHolder(private val mBinding:ItemCategoryBinding): RecyclerView.ViewHolder(mBinding.root){
    fun initView(item: CategoryModel?)  = with(mBinding){
      item?.let {item->
        mBinding.textViewCategoryName.textWithFadeAnimation = item.categoryName
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
      val bind = ItemCategoryBinding.inflate(parent.inflater,parent,false)
      return CategoryViewHolder(bind)
  }

  override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    holder.initView(getItem(position))
  }

  companion object{
    val categoryDiffUtil =  object: DiffUtil.ItemCallback<CategoryModel>(){
      override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean  = oldItem.categoryName == newItem.categoryName

      override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean = oldItem == newItem
    }
  }


}