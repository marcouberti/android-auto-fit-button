# Android Auto Fit Button

Android Auto Fit Button automatically resizes text to fit perfectly within its bounds. It takes into consideration button paddings and drawables.

![](https://raw.githubusercontent.com/marcouberti/android-auto-fit-button/master/images/screen2.png)  

## Setup

### Installation

Add this to your root project *build.gradle* file:  
``` groovy
allprojects {
    repositories {
        jcenter()
        maven {
            url  "http://dl.bintray.com/marcouberti/maven"
        }
    }
}
```

Add this to your app *build.gradle* file:  
``` groovy
dependencies {
    ...
    compile 'com.marcouberti.autofitbutton:android-auto-fit-button:0.1.1@aar'
    ...
}
```

### Basic usage
In order to use the auto fit button you simply have to use it inside your layout XML file like this:

```xml
<com.marcouberti.autofitbutton.AutoFitButton
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:textSize="20sp"
        android:text="CLICK HERE"/>
```

### Custom attributes

There is a custom attribute *minTextSize* you can use to prevent the text become too small. To use it ensure to import the correct namespace:
```xml
xmlns:custom="http://schemas.android.com/apk/res-auto"
```

And than use it like this:
```xml
<com.marcouberti.autofitbutton.AutoFitButton
		custom:minTextSize="4sp"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:textSize="20sp"
        android:text="CLICK HERE"/>
```

## Known issues

- When using *wrap_content* the *height* of the button may not adapt accordingly to the new computed text size. It is strongly suggested to set fixed button dimensions or to use *match_parent*.