package com.example.app.feature.startup

class StartupView(
    private val controller: StartupViewController
) : StartupFeature.View {

    private val presenter: StartupFeature.Presenter = StartupPresenter(StartupModel(), this)

    init {
        presenter.onViewDidLoad()
    }

}
