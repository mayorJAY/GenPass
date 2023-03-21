<p align="center">
  <img src="app_icon.png" title="App Logo">
</p>

# GenPass

GenPass is a secure password generating application designed with a high level of security. It uses Java Security library to generate strong password combinations of either alphabets, numbers, alphanumeric or alphanumeric + standard password special characters. The length of the password can be between 16 and 64. Once a Password is generated, a user can copy it for use. It is not a Password manager, hence for security reasons, the application only keeps track of the 10 most recent passwords generated (with the time of generation) and gets rid of every other record. Also, GenPass does not have any knowledge of what the user uses their generated passwords for. It is implemented using Clean Architecture, Model-View-ViewModel pattern (MVVM) and uses Modern Android Development pattern and libraries. Jetpack Datastore + Coroutines + Flow is used to save and fetch the data. Queue data structure and algorithm was implemented to enforce a maximum number of passwords to be saved per time and a sequential kick out of old data.

## Project Characteristics

This application has the following characteristics:
* 100% Kotlin
* Modern Architecture (Clean Architecture, Model-View-ViewModel)
* [Android Jetpack Components](https://developer.android.com/jetpack)
* [Material Design](https://material.io/develop/android/docs/getting-started)

## Tech Stack

Minimum API level is set to 21, this means GenPass can run on approximately 98% of Android devices
* [Splash Screen](https://developer.android.com/develop/ui/views/launch/splash-screen), the standard recommended Splash Screen library for Android Applications
* [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore) used for data storage and retrieval
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) used to perform asynchronous calls to the device storage
* [Kotlin Flow](https://developer.android.com/kotlin/flow) used to collect the data from the device storage and transforms it to LiveData
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) used to store and manage UI-related data in a lifecycle conscious way
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) which is an observable data holder class used to handle data in a lifecycle-aware manner
* [View Binding](https://developer.android.com/topic/libraries/view-binding) used to easily write code that interacts with views by referencing them directly
* [SDP/SSP](https://github.com/intuit/sdp) which is a scalable size unit that scales with the screen size. It helps to easily design for multiple screens
* [JUnit4](https://junit.org/junit4), a testing framework used for writing unit tests

## License
```
MIT License
Copyright (c) 2023 Joseph Olugbohunmi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
