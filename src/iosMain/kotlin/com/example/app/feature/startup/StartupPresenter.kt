package com.example.app.feature.startup

import com.example.app.util.UIScope
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.random.nextLong

class StartupPresenter(
    private val model: StartupFeature.Model,
    private val view: StartupFeature.View
) : StartupFeature.Presenter, CoroutineScope by UIScope() {

    private var count: Int = 0
        set(value) {
            field = value
            view.setCount(value)
        }

    override fun onViewDidLoad() {
        repeat(50) {
            doRandomRequests()
        }
    }

    private fun doRandomRequests() {
        launch(CoroutineExceptionHandler { _, e ->
            if (e is CancellationException) return@CoroutineExceptionHandler
            println("Error in StartupPresenter coroutine")
            e.printStackTrace()
        }) {
            do try {
                println(model.getDummyJson().toString())
                count++
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                delay(Random.nextLong(2000..6000L))
            } while (true)
        }
    }

}
