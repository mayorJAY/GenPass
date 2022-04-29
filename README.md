<p align="center">
  <img src="app_icon.png" title="App Logo">
</p>

# GenPass

GenPass is a secure password generating application designed with a high level of security. It uses Java Security library to generate strong password combinations of either alphabets, numbers, alphanumeric or alphanumeric + standard password special characters. The length of the password can be between 16 and 64. Once a Password is generated, a user can copy it for use. It is not a Password manager, hence for security reasons, the application only keeps track of the 10 most recent passwords generated (with the time of generation) and gets rid of every other record. Also, GenPass does not have any knowlegde of what the user uses their generated passwords for. It is implemented using Clean Architecture, Model-View-ViewModel pattern (MVVM) and uses Modern Android Development pattern and libraries. Jetpack Datastore + Coroutines + Flow is used to save and fetch the data. Queue data structure and algorithm was implemented to enforce a maximum number of passwords to be saved per time and a sequential kick out of old data.

## Project Characteristics

This application has the following characterisitcs:
* 100% Kotlin
* Modern Architecture (Clean Architecture, Model-View-ViewModel)
* [Android Jetpack Components](https://developer.android.com/jetpack)
* [Material Design](https://material.io/develop/android/docs/getting-started)

## Tech Stack

Minimum API level is set to 21, this means GenPass can run on approximately 98% of Android devices
* [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore) used for data storage and retrieval
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) used to perform asynchronous calls to the device storage
* [Kotlin Flow](https://developer.android.com/kotlin/flow) used to collect the data from the device storage and transforms it to LiveData
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) used to store and manage UI-related data in a lifecycle conscious way
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) which is an observable data holder class used to handle data in a lifecycle-aware manner
* [View Binding](https://developer.android.com/topic/libraries/view-binding) used to easily write code that interacts with views by referencing them directly
* [SDP/SSP](https://github.com/intuit/sdp) which is a scalable size unit that scales with the screen size. It helps to easily design for multiple screens
* [JUnit4](https://junit.org/junit4), a testing framework used for writing unit tests
