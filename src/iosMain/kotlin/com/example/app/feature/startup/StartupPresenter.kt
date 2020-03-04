package com.example.app.feature.startup

import com.example.app.util.UIScope
import kotlinx.coroutines.*

class StartupPresenter(
    private val model: StartupFeature.Model,
    private val view: StartupFeature.View
) : StartupFeature.Presenter, CoroutineScope by UIScope() {

    override fun onViewDidLoad() {
        launch(CoroutineExceptionHandler { _, e ->
            if (e is CancellationException) return@CoroutineExceptionHandler
            println("Error in StartupPresenter coroutine")
            e.printStackTrace()
        }) {
            while (true) {
                try {
                    model.getDummyJson()
                } catch (_: Throwable) {
                }
                delay(100)
            }
        }
    }

}
