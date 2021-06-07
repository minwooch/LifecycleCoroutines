package com.applsh.lifecyclecoroutines.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class MainViewModel : ViewModel() {

    private val channel = Channel<String>(64)
    val flow = channel.receiveAsFlow()

    private val order = AtomicInteger(0)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            channel.send("init 0")
        }

        repeatEvent()
    }

    private fun repeatEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val orderNumber = order.getAndIncrement()
                val eventString = "event: $orderNumber"
                Log.v(MainFragment.TAG, "ViewModel - flow에 $eventString 보냄")
                channel.send(eventString)
                delay(50L)
            }
        }
    }

    fun sendEvent(eventName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val orderNumber = order.getAndIncrement()
            val eventString = "event - $eventName $orderNumber"
            Log.v(MainFragment.TAG, "ViewModel - flow에 $eventString 보내기 전")
            delay(20L)
            channel.send(eventString)
            Log.v(MainFragment.TAG, "ViewModel - flow에 $eventString 보낸 후")
        }
    }
}