package com.enciyo.pinbook.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


var ui: CoroutineDispatcher = Dispatchers.Main
var io: CoroutineDispatcher =  Dispatchers.IO
var background: CoroutineDispatcher = Dispatchers.Default

fun ViewModel.uiJob(block: suspend CoroutineScope.() -> Unit): Job {
  return viewModelScope.launch(ui) {
    block()
  }
}

fun ViewModel.ioJob(block: suspend CoroutineScope.() -> Unit): Job {
  return viewModelScope.launch(io) {
    block()
  }
}

fun ViewModel.backgroundJob(block: suspend CoroutineScope.() -> Unit): Job {
  return viewModelScope.launch(background) {
    block()
  }
}
