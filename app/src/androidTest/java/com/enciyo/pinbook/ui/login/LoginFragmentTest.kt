package com.enciyo.pinbook.ui.login



import android.content.ComponentName
import android.content.Intent
import androidx.core.util.Preconditions
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.enciyo.pinbook.HiltTestActivity
import com.enciyo.pinbook.R
import com.enciyo.pinbook.domain.usecases.LoginUseCase
import com.tencent.mmkv.MMKV
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class LoginFragmentTest{

  @get:Rule
  public var rule = HiltAndroidRule(this)


  @Inject
  lateinit var mUsecase:LoginUseCase
  lateinit var mViewModel:LoginViewModel

  lateinit var mLoginFragment:LoginFragment


  @Before
  fun init(){
    MMKV.initialize(InstrumentationRegistry.getInstrumentation().context)
    rule.inject()
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY,  R.style.FragmentScenarioEmptyFragmentActivityTheme)

    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity {
      mLoginFragment = it.supportFragmentManager.fragmentFactory.instantiate(
          Preconditions.checkNotNull(LoginFragment::class.java.classLoader),
          LoginFragment::class.java.name
      ) as LoginFragment

      it.supportFragmentManager
          .beginTransaction()
          .add(android.R.id.content, mLoginFragment, "")
          .commitNow()

    }



  }


  @Test
  fun testEvent() {

    Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("enciyo"))
    Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText(""),ViewActions.closeSoftKeyboard())
    Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click())
    assertNotNull(mLoginFragment.mPinToast.toastMessage.value)
    Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("2121"),ViewActions.clearText())
    Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("2121"),ViewActions.closeSoftKeyboard())
    assertNotNull(mLoginFragment.mViewModel.viewState.value)
  }

}






