# kNOw Corona
![Github Banner](images/app_banner.png)

**kNOw Corona** is an android powered application for Android Automotive infotainment devices ![](images/and_infotainment.jpg). 
Developed using latest Android Architecture componenets + Car API data(Emulator control - Car sensor data) utilization.

Its an real time application fetches latest information about Covid19 from the world, thanks to [NovelCovid](https://github.com/NovelCOVID/API) for the API.


## Features 🧾 
- Global Cases : Shows Total Covid-19 cases in the world from API
- Country : User can search for a particular country to get latest Total, Recovered and Deceased cases. 
- Map view : Shows Covid-19 cases in an map view
- Covid News : Shows latest news specifically about Covid-19, user can see the news in detailed by clicking it.

## Automotive Features 🚗
- RSS TTS : If the user was in News Page and trying to access the car by ignition ON, the application will automatically starts reading(Text to speech) the news.

> Criteria : Ignition ON + (Vehicle Speed > 10)
- Permissions

`
<!-- Android Car Api Permissions -->
    <uses-permission android:name="android.car.permission.CAR_SPEED" />
    <uses-permission android:name="android.car.permission.CAR_POWERTRAIN" />
`


## Screenshots 📷

| <img src="images/app_scrrenshot_hmi.png" width="350" height="210">   |      <img src="images/app_screenshot_splash.png" width="350" height="210">      |  <img src="images/app_screenshot_home.png" width="350" height="210"> |
|:----------:|:-------------:|:------:|
| *AA HMI* |  *Splash HMI* | *Home HMI* |

| <img src="images/app_screenshot_countries.png" width="350" height="210">   |      <img src="images/app_screenshot_search_country.png" width="350" height="210">      |  <img src="images/app_screenshot_search_country2.png" width="350" height="210"> |
|:----------:|:-------------:|:------:|
| *Countries HMI* |  *Search Country HMI* | *Search Country Result HMI* |

| <img src="images/app_screenshot_map.png" width="350" height="210">   |      <img src="images/app_screenshot_rss.png" width="350" height="210">      |  <img src="images/app_screenshot_rss_detailed_vehON.png" width="350" height="210"> |
|:----------:|:-------------:|:------:|
| *Map HMI* |  *RSS-Corona News HMI* | *RSS - Detailed(Vehicle Criteria ON) HMI* |

## Demo 🎥
| <img src="images/app_demo_v1.gif" width="550" height="410"> |
|:----------:|

## AA Getting Started 📖

Android Automotive is the new embedded, fully functional operating system for vehicles. 
It contains a bunch of API packages allowing us to integrate Android OS functionalities with our car.
[Read More...](https://source.android.com/devices/automotive)

To build this application, we should aware of AOSP build [(Android Open Source Project)](https://source.android.com/)

Here I have used latest AOSP ROM to build android service jar.

If using jar is complicated please use this [Linux SDK](https://gitlab.com/filipmg/android-automotive-sdk) to build.


## Build with 🛠️

- Java
- Android Architecture Components 
    - LiveData - Data objects that notify views when the underlying database changes.
    - ViewModel - Stores UI-related data that isn't destroyed on UI changes.
- Retrofit - A type-safe HTTP client for Android and Java.
- Room - A persistence library provides an abstraction layer over SQLite
- Car API - Bunch of apis from Android specifically used to communicate with car data.


