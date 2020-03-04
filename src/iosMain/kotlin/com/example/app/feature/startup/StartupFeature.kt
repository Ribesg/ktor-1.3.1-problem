package com.example.app.feature.startup

import kotlinx.serialization.json.JsonElement

interface StartupFeature {

    interface Model {

        suspend fun getDummyJson(): JsonElement

    }

    interface View {
        
        fun setCount(count: Int)
        
    }

    interface Presenter {

        fun onViewDidLoad()

    }

}
