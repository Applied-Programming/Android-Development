# Go_Ubiquitous

This project is the 6th project in the Android Developer Nanodegree which involves building a wearable watchface for the <a href="https://github.com/udacity/Advanced_Android_Development/tree/7.05_Pretty_Wallpaper_Time">Udacity Sunshine App</a> to run on an Android Wear device.

To build and run the Sunshine app on your mobile device or the wearable, you'll need an API key from http://openweathermap.org/.

First, sign up at https://home.openweathermap.org/users/sign_up
Then select API from the menu and follow to create your own API key.

Next up in our code files, add your API key to the code by changing your ~/app/build.gradle file:

Enter your API key in place of Your-Own-API-Key in the following block of code:

<code>  
buildTypes.each {
	it.buildConfigField 'String', 'OPEN_WEATHER_MAP_API_KEY', '\"Your-Own-API-Key\"'
}
</code>

Once you have replaced Your-Own-API-Key, you should be good to go!

<hr>

## Rubric:

* App works on both round and square face watches.
* App displays the current time.
* App displays the high and low temperatures.
* App displays a graphic that summarizes the day’s weather (e.g., a sunny image, rainy image, cloudy image, etc.).
* App conforms to common standards found in the Android Nanodegree General Project Guidelines.



<hr>
