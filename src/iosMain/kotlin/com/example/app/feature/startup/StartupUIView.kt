package com.example.app.feature.startup

import kotlinx.cinterop.CValue
import platform.CoreGraphics.CGRect
import platform.UIKit.*

class StartupUIView(frame: CValue<CGRect>) : UIView(frame) {

    val label = UILabel().apply {
        translatesAutoresizingMaskIntoConstraints = false
        backgroundColor = UIColor.yellowColor
        text = "0"
        textColor = UIColor.purpleColor
        font = font.fontWithSize(300.0)
        textAlignment = NSTextAlignmentCenter
    }

    init {
        backgroundColor = UIColor.purpleColor

        addSubview(label)

        topAnchor.constraintEqualToAnchor(label.topAnchor).apply { constant = -20.0 }.active = true
        bottomAnchor.constraintEqualToAnchor(label.bottomAnchor).apply { constant = 20.0 }.active = true
        leadingAnchor.constraintEqualToAnchor(label.leadingAnchor).apply { constant = -20.0 }.active = true
        trailingAnchor.constraintEqualToAnchor(label.trailingAnchor).apply { constant = 20.0 }.active = true
    }

}
