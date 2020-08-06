package com.enciyo.pinbook.reducer

import com.enciyo.pinbook.ui.dashboard.states.DashboardActionState
import com.enciyo.pinbook.ui.dashboard.states.DashboardSideEffect
import com.enciyo.pinbook.ui.dashboard.states.DashboardViewState
import com.enciyo.pinbook.ui.login.LoginActionState
import com.enciyo.pinbook.ui.login.LoginSideEffect
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
  fun provideLoginReducer() = ReducerImp.create<LoginViewState,LoginActionState,LoginSideEffect>("LoginViewModel")


  @Provides
  @Redux
  fun provideDashboardReducer() = ReducerImp.create<DashboardViewState,DashboardActionState,DashboardSideEffect>("DashboardViewModel")


}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Redux


