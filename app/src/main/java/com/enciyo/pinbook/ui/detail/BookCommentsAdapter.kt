package com.enciyo.pinbook.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.enciyo.pinbook.databinding.ItemCommentBinding
import com.enciyo.pinbook.domain.model.Comments
import com.enciyo.pinbook.utils.inflater
import com.enciyo.pinbook.utils.textWithFadeAnimation


class BookCommentsAdapter : ListAdapter<Comments, BookCommentsAdapter.BookCommentsViewHolder>(CommentsDiffUtils) {


    inner class BookCommentsViewHolder(val mBinding: ItemCommentBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun initView(item: Comments?) {
            item?.let {
                mBinding.textViewDisplayName.textWithFadeAnimation = it.commentDisplayName
                mBinding.textViewInformation.textWithFadeAnimation = it.comment
            }
        }

    }


    companion object {
        object CommentsDiffUtils : DiffUtil.ItemCallback<Comments>() {
            override fun areItemsTheSame(oldItem: Comments, newItem: Comments): Boolean =
                oldItem.commentDisplayName == newItem.commentDisplayName

            override fun areContentsTheSame(oldItem: Comments, newItem: Comments): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookCommentsViewHolder {
        return BookCommentsViewHolder(ItemCommentBinding.inflate(parent.inflater,parent,false))
    }

    override fun onBindViewHolder(holder: BookCommentsViewHolder, position: Int) {
        holder.initView(getItem(position))
    }


}