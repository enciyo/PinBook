package com.enciyo.pinbook.reducer

import com.enciyo.pinbook.ui.books.state.BookActionState
import com.enciyo.pinbook.ui.books.state.BookRepoState
import com.enciyo.pinbook.ui.books.state.BookViewState
import com.enciyo.pinbook.ui.dashboard.states.DashboardActionState
import com.enciyo.pinbook.ui.dashboard.states.DashboardRepoState
import com.enciyo.pinbook.ui.dashboard.states.DashboardViewState
import com.enciyo.pinbook.ui.detail.BookDetailActionState
import com.enciyo.pinbook.ui.detail.BookDetailRepoState
import com.enciyo.pinbook.ui.detail.BookDetailViewState
import com.enciyo.pinbook.ui.fav.FavoriteActionState
import com.enciyo.pinbook.ui.fav.FavoriteRepoState
import com.enciyo.pinbook.ui.fav.FavoriteViewState
import com.enciyo.pinbook.ui.login.LoginActionState
import com.enciyo.pinbook.ui.login.LoginRepoState
import com.enciyo.pinbook.ui.login.LoginViewState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Qualifier


@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object ReducerModule {


    @Provides
    @Redux
    fun provideLoginReducer() =
        ReducerImp.create<LoginViewState, LoginActionState, LoginRepoState>("LoginViewModel") as Reducer<LoginViewState, LoginActionState, LoginRepoState>


    @Provides
    @Redux
    fun provideDashboardReducer() =
        ReducerImp.create<DashboardViewState, DashboardActionState, DashboardRepoState>("DashboardViewModel")

    @Provides
    @Redux
    fun provideFavoriteReducer() =
        ReducerImp.create<FavoriteViewState, FavoriteActionState, FavoriteRepoState>("FavoriteReducer")

    @Provides
    @Redux
    fun provideBookReducer() =
        ReducerImp.create<BookViewState, BookActionState, BookRepoState>("BookReducer")

    @Provides
    @Redux
    fun provideBookDetailReducer() =
        ReducerImp.create<BookDetailViewState,BookDetailActionState,BookDetailRepoState>("BookDetailReducer")


}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Redux


