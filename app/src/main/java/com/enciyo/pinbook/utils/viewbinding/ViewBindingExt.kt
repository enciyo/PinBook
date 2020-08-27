package com.enciyo.pinbook.utils.viewbinding

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


@PublishedApi
internal class FragmentViewBindingProperty<F : Fragment, T : ViewBinding>(
    viewBinder: (F) -> T
) : ViewBindingProperty<F, T>(viewBinder) {
  override fun getLifecycleOwner(thisRef: F) = thisRef.viewLifecycleOwner
}

@Suppress("unused")
@JvmName("viewBindingFragment")
inline fun <F : Fragment, reified T : ViewBinding> F.viewBinding(): ViewBindingProperty<Fragment, T> {
  return viewBinding(FragmentViewBinder(T::class.java)::bind)
}



@Suppress("unused")
@JvmName("viewBindingFragment")
fun <F : Fragment, T : ViewBinding> F.viewBinding(viewBinder: (F) -> T): ViewBindingProperty<F, T> {
  return FragmentViewBindingProperty(viewBinder)
}



