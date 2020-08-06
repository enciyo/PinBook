package com.enciyo.pinbook.reducer




interface HasUserIntercession<V : UserInteractions> {
  fun onEvent(event: V)
}