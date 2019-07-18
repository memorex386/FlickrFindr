# Flickr Findr

FLickr Findr is a one stop shop for searching images on Flickr.  Features include :

* A free text search function
* Sorting options
* Bookmark option to save favorites and store offline
* Recent Searches for quick searching

## Core Dev Logic
The app is a MVVM or MVI approach.  Worked hard on seperating ui with core logic, and creating modular approaches.  Other features include :

* Android Arch View Models
* Live Data
* a custom Live Data Action to recieve Lifecycle aware events
* Shared Transistions
* Kotlin Coroutines with lifecycle awareness and suspend fuctions
* My JAVA classes are TestJavaActivity, TestJavaViewModel, and my StringDef class called PhotoSort. (NOTE that the Test classes dont run in the app, but I built them to show that I can write the same class in Kotlin or in Java including the operability between the two languages)
* OKHTTP, Retrofit, with Kotlin Coroutines and suspend
* Room for Bookmarking logic
* Storing images into file storage and retrieving from storage
* A few extension KT classes for custom extensions
* MOSHI for object parsing
* Glide for image loading
* Constraint Layouts
* Custom view for search queries 
* Used some core Kotlin featues like inline functions, delegates, and more.

## Things I would improve with more time

* Shared Element Transitions - it's a bit clunky right now
* Bookmarking - it's fairly slow loading to file storage and has a bit of a delay on clicking the button
* Adding more search functionality - things like choosing free text or tag searchs, and adding other functionality that is listed in their apis.
* Unit Tests
* Recycler View Data Set Changed Animation - the Android core animation for this in the Bookmark Activity is acting weird.

##Thank You!

Please check out my other sample app at [https://github.com/memorex386/github_example](https://github.com/memorex386/github_example)  where I used other tools like :

* RxJava
* Fragments
* ViewPagers

Thanks for your time and for reviewing my application!