package com.example.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class UIScope : CoroutineScope {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = UIDispatcher() + job

}
