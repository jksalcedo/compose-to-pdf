package com.jksalcedo.app

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.util.concurrent.atomic.AtomicInteger

class PdfImageMonitor {
    private val loadingCount = AtomicInteger(0)
    val isIdle = MutableStateFlow(true)

    fun notifyLoadingStarted() {
        loadingCount.incrementAndGet()
        isIdle.value = false
    }

    fun notifyLoadingFinished() {
        val current = loadingCount.decrementAndGet()
        if (current == 0) {
            isIdle.value = true
        }
    }

    suspend fun waitForImages() {
        // Wait until the flow is true
        isIdle.first { it }

        // Extra delay to ensure rendering completes
        delay(200)
    }
}