@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.example.app.util

import kotlinx.coroutines.*
import platform.darwin.*
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.AtomicReference

@UseExperimental(InternalCoroutinesApi::class)
class UIDispatcher : CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) {
            block.run()
        }
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        delayOnMainQueue(timeMillis) {
            with(continuation) {
                resumeUndispatched(Unit)
            }
        }
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle {
        val handle = object : DisposableHandle {

            val disposed = AtomicReference(false)

            override fun dispose() {
                disposed.compareAndSet(expected = false, new = true)
            }

        }
        delayOnMainQueue(timeMillis) {
            if (!handle.disposed.value) {
                block.run()
            }
        }
        return handle
    }

    private fun delayOnMainQueue(delayMs: Long, task: () -> Unit) {
        dispatch_after(
            dispatch_time(DISPATCH_TIME_NOW, delayMs * 1_000_000),
            dispatch_get_main_queue(),
            task
        )
    }

}
