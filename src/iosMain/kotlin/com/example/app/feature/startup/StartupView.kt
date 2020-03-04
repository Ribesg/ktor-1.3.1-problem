package com.example.app.feature.startup

class StartupView(
    private val controller: StartupViewController,
    private val uiView: StartupUIView
) : StartupFeature.View {

    private val presenter: StartupFeature.Presenter = StartupPresenter(StartupModel(), this)

    init {
        presenter.onViewDidLoad()
    }

    override fun setCount(count: Int) {
        uiView.label.text = count.toString()
    }

}
