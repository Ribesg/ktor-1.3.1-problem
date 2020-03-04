package com.example.app.feature.startup

interface StartupFeature {

    interface Model {

        suspend fun getDummyJson(): String

    }

    interface View

    interface Presenter {

        fun onViewDidLoad()

    }

}
