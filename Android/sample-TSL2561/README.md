TSL2561 - Light sensor.
=======================
 
  Driver files and sample NDK based (console) application for the Groove Light sensor (TSL2561).

## To build: 
 - Download a recent Android NDK build ( http://developer.android.com/ndk/downloads/index.html) 
 - Include the NDK installation folder to PATH variable.
 - Now run ndk-build to create the image files.
 
 
## To Test:
 - Connect the Grove sensor with I2C interface. See Interface diagram below.
 - adb root
 - adb remount
 - adb push libs/arm64-v8a/samples-TSL2561 /system/bin
 - adb shell chmod 777 /system/bin/samples-TSL2561
 
## Observe: 
 - If successful you should see a series of prints with the LUX that is detected.. 
 
## Sensor hardware setup:
 ![](./../images/410c_I2C_interface.png)
  
