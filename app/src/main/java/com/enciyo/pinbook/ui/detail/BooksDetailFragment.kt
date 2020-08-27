package com.enciyo.pinbook.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.enciyo.pinbook.R
import com.enciyo.pinbook.databinding.FragmentBooksDetailBinding
import com.enciyo.pinbook.di.GlideApp
import com.enciyo.pinbook.domain.model.BookDetail
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.reducer.render
import com.enciyo.pinbook.utils.onEach
import com.enciyo.pinbook.utils.setOnClickListener
import com.enciyo.pinbook.utils.textWithFadeAnimation
import com.enciyo.pinbook.utils.viewbinding.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BooksDetailFragment : BottomSheetDialogFragment() {

    private val mBinding: FragmentBooksDetailBinding by viewBinding()
    private val mViewModel: BooksDetailViewModel by viewModels()
    private val mCommentAdapter: BookCommentsAdapter = BookCommentsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mViewModel) {
            reducer
                .render(
                    lifecycle = viewLifecycleOwner,
                    onState = ::renderBookDetailState,
                    onActionIfNotHandled = ::renderBookDetailActionState,
                    onRepo = ::renderBookDetailRepoState
                )
        }

        with(mBinding) {
            recyclerViewComments.adapter = mCommentAdapter
            floatingActionButtonFavorite.setOnClickListener(::floatingActionButtonFavoriteClicked)
            imageButtonSend.setOnClickListener(::imageButtonSendClicked)
        }


        getArgBook()?.also {
            mViewModel.onEvent(BookDetailUserInteractions.Init(it.id))
        }


    }

    private fun floatingActionButtonFavoriteClicked(){
        getArgBook()?.let {
            mViewModel.onEvent(BookDetailUserInteractions.OnClickedFav(it))
        }

    }

    private fun getArgBook() =
        arguments?.getParcelable<PopularBooks>("Books")

    private fun imageButtonSendClicked() {
        val bookId: Int = getArgBook()?.id ?: return
        val comment = mBinding.editTextComment.text.toString()
        val rating = mBinding.ratingBar.rating.toDouble()
        mViewModel.onEvent(BookDetailUserInteractions.RateBook(bookId, rating, comment))
        findNavController().previousBackStackEntry?.savedStateHandle?.set("isRated", true)
    }


    private fun renderBookDetailState(viewState: BookDetailViewState) {
        with(mBinding) {
            mBinding.editTextComment.isEnabled = viewState.isCommentEnable
            mBinding.ratingBar.isEnabled = viewState.isCommentEnable
            mBinding.imageButtonSend.isEnabled = viewState.isCommentEnable
            mBinding.ratingBar.rating = viewState.ratingBar
            mBinding.floatingActionButtonFavorite.imageTintList = viewState.getFavoriteBackgroundColor(requireContext())
        }
    }

    private fun renderBookDetailActionState(actionState: BookDetailActionState) {

    }

    private fun renderBookDetailRepoState(repoState: BookDetailRepoState) {
        repoState.bookDetail.asFlow().onEach(::renderBookDetail)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        repoState.commentResult.asFlow().onEach(::renderCommentResult)
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }


    private fun renderCommentResult(isSuccess: String) {
        mViewModel.onEvent(BookDetailUserInteractions.Refresh(getArgBook()?.id ?: return))

    }

    private fun renderBookDetail(detail: BookDetail) {
        with(mBinding) {
            GlideApp.with(root)
                .load(detail.imageUrl)
                .transition(withCrossFade())
                .apply(RequestOptions().transform(RoundedCorners(45)))
                .into(imageViewBook)
            textViewBookName.textWithFadeAnimation = detail.name
            textViewBookAuthor.textWithFadeAnimation = detail.author
            textViewBookContent.textWithFadeAnimation = detail.mInformation
            mCommentAdapter.submitList(detail.comments)
        }
    }


}