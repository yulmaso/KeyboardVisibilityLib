# KeyboardVisibilityLib
[![](https://jitpack.io/v/yulmaso/KeyboardVisibilityLib.svg)](https://jitpack.io/#yulmaso/KeyboardVisibilityLib)

Small keyboard visibility listener library for Android. Requires API >= 21. Doesn't work with windowSoftInputMode="adjustNothing". 

- Simple in use with extension functions of Activity and Fragment
- Automatically removes listener when its parent Activity or Fragment is destroyed.
- Supports manual listener removal.

## Installation:

Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.yulmaso:KeyboardVisibilityLib:Tag'
}
```

## Usage in Activity:

```kotlin
setKeyboardVisibilityListener(lifecycle) { visible ->
    // That's a callback body. Do whatever depends on keyboard visibility.
}
```

## Usage in Fragment:

```kotlin
setKeyboardVisibilityListener(viewLifecycleOwner) { visible ->
    // That's a callback body. Do whatever depends on keyboard visibility.
}
```

## Manual listener removal:

```kotlin
// Get listener instance
val keyboardListener = setKeyboardVisibilityListener(viewLifecycleOwner) { visible ->
    // That's a callback body. Do whatever depends on keyboard visibility.
}

// Remove the listener when you don't need it anymore
keyboardListener.removeKeyboardListener()
```
