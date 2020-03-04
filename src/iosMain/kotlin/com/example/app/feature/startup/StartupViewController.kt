package com.example.app.feature.startup

import platform.UIKit.UIViewController

class StartupViewController : UIViewController(null, null) {

    private lateinit var featureView: StartupView

    override fun loadView() {
        super.loadView()
        view = StartupUIView(view.frame)
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        featureView = StartupView(this, view as StartupUIView)
    }

}
