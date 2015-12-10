Tcs3414cs - I2C Color sensor
===========================

 Driver files and sample NDK based (console) application for the Groove Color sensor (Tcs3414cs).

## To build: 
 - Download a recent Android NDK build ( http://developer.android.com/ndk/downloads/index.html) 
 - Include the NDK installation folder to PATH variable.
 - Now run ndk-build to create the image files.
 
 
## To Test:
 - Connect the Grove sensor with I2C interface. See Interface diagram below.
 - adb root
 - adb remount
 - adb push libs/arm64-v8a/samples-Tcs3414cs /system/bin
 - adb shell chmod 777 /system/bin/samples-Tcs3414cs
 
## Observe: 
 - If sucessful you should see a series of prints with R G B values detected by the color sensor. 

## Sensor hardware setup:
 ![](./../images/410c_I2C_interface.png)
 
  
