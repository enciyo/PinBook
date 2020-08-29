package com.enciyo.pinbook.common.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.enciyo.pinbook.databinding.ItemBooksBinding
import com.enciyo.pinbook.di.GlideApp
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.utils.inflater
import com.enciyo.pinbook.utils.textWithFadeAnimation
import java.util.*


class BooksAdapter :
    ListAdapter<PopularBooks, BooksAdapter.PopularBooksViewHolder>(popularBooksDiffUtils), Filterable {


    private var mOriginalData: List<PopularBooks> = listOf()
    private var mFilteredList: List<PopularBooks> = listOf()
    var mListener : ((BooksAdapterEvents)->Unit)? = null

    inner class PopularBooksViewHolder(private val mBinding: ItemBooksBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun initView(item: PopularBooks?) = with(mBinding) {
            GlideApp.with(root).load(item?.imageUrl).into(imageViewBookImage)
            textViewBookAuthor.textWithFadeAnimation = item?.author
            textViewBookName.textWithFadeAnimation = item?.name
            textViewBookRating.textWithFadeAnimation = item?.rating
            textViewReviewCount.textWithFadeAnimation = item?.reviewCount
            root.setOnClickListener {
                item?.let {
                    mListener?.invoke(BooksAdapterEvents.Clicked(item))
                }
            }
        }
    }


    fun setList(list: MutableList<PopularBooks>?){
        mOriginalData = list ?: listOf()
        submitList(list)
    }

    fun setFilterList(list: MutableList<PopularBooks>?){
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBooksViewHolder {
        val bind = ItemBooksBinding.inflate(parent.inflater, parent, false)
        return PopularBooksViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PopularBooksViewHolder, position: Int) {
        holder.initView(getItem(position))
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                mFilteredList = applyFilterToList(constraint.toString())
                val filterResult = FilterResults()
                filterResult.values = mFilteredList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mFilteredList = results?.values as? List<PopularBooks> ?: return
                setFilterList(mFilteredList.toMutableList())
            }

        }
    }

    private fun applyFilterToList(charString: String): List<PopularBooks> {
        return if (charString.isBlank()) mOriginalData
        else
            mOriginalData.filter {
                it.name.toLowerCase(Locale.ENGLISH)
                    .contains(charString.toLowerCase(Locale.ENGLISH)) ||
                        it.author.toLowerCase(Locale.ENGLISH)
                            .contains(charString.toLowerCase(Locale.ENGLISH))
            }
                .toList()
    }

    companion object {
        val popularBooksDiffUtils
            get() = object : DiffUtil.ItemCallback<PopularBooks>() {
                override fun areItemsTheSame(
                    oldItem: PopularBooks,
                    newItem: PopularBooks
                ): Boolean = oldItem.name == newItem.name

                override fun areContentsTheSame(
                    oldItem: PopularBooks,
                    newItem: PopularBooks
                ): Boolean = oldItem == newItem
            }

        sealed class BooksAdapterEvents{
            data class Clicked(val books: PopularBooks) : BooksAdapterEvents()
        }

    }



}



