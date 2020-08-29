package com.enciyo.pinbook.ui.detail

import com.enciyo.pinbook.reducer.ActionState


sealed class BookDetailActionState: ActionState(){
    object ShowSuccessAddedFavoriteBook : BookDetailActionState()
    object ShowSuccessRemovedFavoriteBook : BookDetailActionState()
}