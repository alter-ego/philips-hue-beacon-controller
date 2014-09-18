#Philips Hue Beacon Controller

This is an example of the app that uses iBeacons for home automation control, namely Philips Hue lights. I wanted to see what can be done and how it works on Android because BLE is supported only from 4.3 onwards and iBeacons are not supported natively as such, unlike on iOS.

It is a partial implementation of [Philips Hue API](http://www.developers.meethue.com/documentation/getting-started) and I hope to be able to implement it all.

## PHILIPS HUE API IMPLEMENTATION

Status as of version **0.1**:

* Lights - partial
* Groups - TODO
* Schedules - TODO
* Scenes - TODO
* Sensors - TODO
* Rules - TODO
* Configuration - partial
* Info - TODO

## FUTURE

First I would like to implement all the features of the Philips Hue API. Secondly, I would like to reimplement the iBeacon API because it's fairly limited as it is (no support for writing data) and also implement the rest of the BLE standard (various kinds of sensor recognition). 

Also I would like to check out the [AltBeacon](https://github.com/AltBeacon/spec) protocol.

## COMPATIBILITY

Android 4.3 (API 18) and up.

## DEPENDENCIES

* Radius Networks' [AndroidIBeaconLibrary](https://github.com/AltBeacon/android-beacon-library) for iBeacon (version 0.7.7)
* [Lombok](http://projectlombok.org/) for getters, setters and such
* [JodaTime](http://www.joda.org/joda-time/) for Date management
* [TypedPreferences](https://github.com/johnjohndoe/TypedPreferences) for saving/reading preferences
* [Gson](https://code.google.com/p/google-gson/) for JSON manipulation
* [RxJava](https://github.com/ReactiveX/RxJava/wiki) for reactive programming
* [UniversalImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader) for image loading
* [Retrofit](http://square.github.io/retrofit/) for REST interface
* [OKHttp](http://square.github.io/okhttp/) for HTTP communication
* [GreenDAO](http://greendao-orm.com/) for DAO
* [SuperToasts](https://github.com/JohnPersano/SuperToasts) for improved toast displaying
* AndroidBound - private viewbinding library

## CONTRIBUTORS

- Sasa Sekulic

## LICENSE

Copyright (C) 2014 Alter Ego Srls.

This library is licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).