package com.enciyo.pinbook.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.enciyo.pinbook.databinding.ItemShowcaseBooksBinding
import com.enciyo.pinbook.di.GlideApp
import com.enciyo.pinbook.domain.model.ShowcaseBooks


class DashboardShowCaseAdapter : PagerAdapter() {


  var currentItems = mutableListOf<ShowcaseBooks>()
  var listener: ((ShowcaseBooks) -> Unit)? = null

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view == `object`
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val binding = ItemShowcaseBooksBinding.inflate(LayoutInflater.from(container.context), container, false)
    val book = currentItems[position]
    val viewHolder = ShowCaseViewHolder(binding)
    viewHolder.initView(book)
    container.addView(binding.root)
    return binding.root
  }

  override fun getCount(): Int = currentItems.size

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

  }


  inner class ShowCaseViewHolder(private val mBinding: ItemShowcaseBooksBinding) : RecyclerView.ViewHolder(mBinding.root) {
    fun initView(book: ShowcaseBooks) {
      GlideApp.with(mBinding.root).load(book.imageUrl).into(mBinding.imageViewBook)
      mBinding.root.setOnClickListener {
        listener?.invoke(book)
      }
    }
  }

}


