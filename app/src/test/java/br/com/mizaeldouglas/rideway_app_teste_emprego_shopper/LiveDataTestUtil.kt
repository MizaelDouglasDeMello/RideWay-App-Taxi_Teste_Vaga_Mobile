package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
object LiveDataTestUtil {
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        try {
            observeForever(observer)

            // Aguarda até que o valor seja emitido ou o tempo acabe
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set within the time limit.")
            }

        } finally {
            removeObserver(observer)
        }

        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}
