package com.enciyo.pinbook.ui.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.enciyo.bottomnavigationgprah.BottomNavigationGraphOnHostChangeCallback
import com.enciyo.bottomnavigationgprah.Builder
import com.enciyo.pinbook.utils.viewbinding.viewBinding
import com.enciyo.pinbook.R
import com.enciyo.pinbook.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

  private val mBinding: FragmentMainBinding by viewBinding()
  private val mViewModel: MainViewModel by viewModels()



  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val navigation = Builder()
        .setLifecycle(lifecycle)
        .setBottomNavigationGraphHostViewPager(mBinding.navigationBaseView)
        .setFragmentManager(childFragmentManager)
        .setBottomNavigationView(mBinding.bottomNavigationView)
        .setGraphs(R.navigation.graph_dashboard, R.navigation.graph_fav, R.navigation.grabh_books)
        .build()

    navigation.onHostChangeCallback = onHostChangeCallback()

    mBinding.buttonFav.setOnClickListener {
      mBinding.bottomNavigationView.selectedItemId = mBinding.bottomNavigationView.menu.children.toMutableList().get(1).itemId
    }


  }

  override fun onPause() {
    super.onPause()
  }


  private fun onHostChangeCallback() = object : BottomNavigationGraphOnHostChangeCallback {
    override fun onNavigationItemReselected(position: Int): Boolean {
      checkFavButtonIsActive(position)
      return true
    }

    override fun onNavigationItemSelected(position: Int): Boolean {
      checkFavButtonIsActive(position)
      return true
    }
  }

  private fun checkFavButtonIsActive(position: Int) {
    if (position == 1) onActiveFavButton()
    else onDisableFavButton()
  }

  private fun onActiveFavButton() {
    mBinding.buttonFav.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.red_300))
  }

  private fun onDisableFavButton() {
    mBinding.buttonFav.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.grey_200))
  }


}