package com.enciyo.pinbook.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.enciyo.pinbook.databinding.ItemBooksBinding
import com.enciyo.pinbook.di.GlideApp
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.utils.inflater
import com.enciyo.pinbook.utils.textWithFadeAnimation


class BooksAdapter : ListAdapter<PopularBooks, BooksAdapter.PopularBooksViewHolder>(popularBooksDiffUtils) {


  class PopularBooksViewHolder(private val mBinding: ItemBooksBinding) : RecyclerView.ViewHolder(mBinding.root) {
    fun initView(item: PopularBooks?) = with(mBinding) {
      GlideApp.with(root).load(item?.imageUrl).into(imageViewBookImage)
      textViewBookAuthor.textWithFadeAnimation = item?.author
      textViewBookName.textWithFadeAnimation = item?.name
      textViewBookRating.textWithFadeAnimation = item?.rating
      textViewReviewCount.textWithFadeAnimation = item?.reviewCount
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBooksViewHolder {
    val bind = ItemBooksBinding.inflate(parent.inflater, parent, false)
    return PopularBooksViewHolder(bind)
  }

  override fun onBindViewHolder(holder: PopularBooksViewHolder, position: Int) {
    holder.initView(getItem(position))
  }

  companion object {
    val popularBooksDiffUtils
      get() = object : DiffUtil.ItemCallback<PopularBooks>() {
        override fun areItemsTheSame(oldItem: PopularBooks, newItem: PopularBooks): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: PopularBooks, newItem: PopularBooks): Boolean = oldItem == newItem
      }
  }


}



