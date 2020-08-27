package com.enciyo.pinbook.di

import com.enciyo.pinbook.common.CoScope
import com.enciyo.pinbook.common.validations.FormValidation
import com.enciyo.pinbook.common.validations.FormValidationImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityComponent::class)
class FormValidationModule {

  @Provides
  @ActivityScoped
  fun provide(
      @CoScope coroutineScope: CoroutineScope
  ) = FormValidationImp(coroutineScope) as FormValidation

}


