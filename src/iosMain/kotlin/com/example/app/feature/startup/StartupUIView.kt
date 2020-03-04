package com.example.app.feature.startup

import kotlinx.cinterop.CValue
import platform.CoreGraphics.CGRect
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.backgroundColor

class StartupUIView(frame: CValue<CGRect>) : UIView(frame) {

    init {
        backgroundColor = UIColor.yellowColor
    }

}
