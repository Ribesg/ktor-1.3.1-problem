## Reproducer for crash in Ktor Client 1.3.1 on iOS

How to:
1. Open `xcode.xcworkspace` in XCode (I use 11.3.1)
2. Run on a device from XCode

The app starts 50 coroutines, each looping and fetching json payloads from a bunch of urls.
After each successful request, a counter is incremented and displayed on the screen.
After a certain amount of requests (anywhere from 5 to 100 in my tests), the app should crash.
If the app does not crash, try restarting it.

For me, it crashes 100% of the time in under 20 seconds when launched on the device from XCode,
but strangely can either quickly crash or never crash when started from the device itself.
I never saw a crash in a simulator.
